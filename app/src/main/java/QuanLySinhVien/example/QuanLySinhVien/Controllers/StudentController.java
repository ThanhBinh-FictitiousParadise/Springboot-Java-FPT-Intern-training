package QuanLySinhVien.example.QuanLySinhVien.Controllers;

import QuanLySinhVien.example.QuanLySinhVien.DTO.StudentDTO;
import QuanLySinhVien.example.QuanLySinhVien.Entities.Registration;
import QuanLySinhVien.example.QuanLySinhVien.Entities.Student;
import QuanLySinhVien.example.QuanLySinhVien.Entities.Subject;
import QuanLySinhVien.example.QuanLySinhVien.Mapper.StudentMapper;
import QuanLySinhVien.example.QuanLySinhVien.Services.RegistrationService;
import QuanLySinhVien.example.QuanLySinhVien.Services.StudentService;
import QuanLySinhVien.example.QuanLySinhVien.Services.SubjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/students")
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    private StudentService studentService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private StudentMapper studentMapper;

    @GetMapping
    public String showStudents(Model model) {
        List<StudentDTO> studentDTOs = studentMapper.toDTOList(studentService.getAllStudents());
        model.addAttribute("currentPage", "students");
        model.addAttribute("students", studentDTOs);
        return "students/list";
    }

    @GetMapping("/add")
    public String showAddStudentForm(Model model) {
        model.addAttribute("currentPage", "students");
        model.addAttribute("suggestedSID", studentService.generateNextSID());
        return "students/add";
    }

    @PostMapping("/add")
    public String addStudent(@RequestParam(required = false) String sid,
                             @RequestParam String name,
                             @RequestParam String email,
                             @RequestParam String dateOfBirth,
                             @RequestParam(required = false) String address,
                             @RequestParam(required = false) String contact,
                             @RequestParam(name = "image", required = false) MultipartFile image,
                             Model model) {

        Student newStudent = new Student();
        newStudent.setName(name);
        newStudent.setEmail(email);

        // Handle the image upload
        String imageFilename = "default_user.png"; // Default image
        if (image != null && !image.isEmpty()) {
            try {
                // Define the absolute path OUTSIDE resources (This is recommended I swear)
                String uploadsDir = System.getProperty("user.dir") + "/uploads";

                // Ensure the directory exists
                Files.createDirectories(Paths.get(uploadsDir));

                // Generate a unique filename
                imageFilename = UUID.randomUUID() + "_" + image.getOriginalFilename();

                // Save the uploaded file
                Path filePath = Paths.get(uploadsDir, imageFilename);
                Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("error", "Failed to upload the image.");
                return "students/add";
            }
        }
        newStudent.setImageFilename(imageFilename);

        // This is for date parsing
        try {
            newStudent.setDateOfBirth(LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd.");
        }

        newStudent.setStatus(1);
        newStudent.setAddress(address);
        newStudent.setContact(contact);

        if (sid == null || sid.isEmpty()) {
            newStudent.setSid(studentService.generateNextSID());
        } else {
            newStudent.setSid(sid);
        }

        try {
            studentService.saveStudent(newStudent);
            return "redirect:/students";
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("Email already exists.")) {
                model.addAttribute("currentPage", "students");
                model.addAttribute("error", "Email already exists.");
                return "students/add";
            } else {
                throw e;
            }
        }
    }



    @GetMapping("/edit/{id}")
    public String showEditStudentForm(@PathVariable Long id, Model model) {
        model.addAttribute("currentPage", "students");
        Student student = studentService.getActiveStudentById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or inactive student Id: " + id));

        StudentDTO studentDTO = studentMapper.toDTO(student);
        model.addAttribute("student", studentDTO);
        return "students/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateStudent(@PathVariable Long id,
                                @RequestParam String name,
                                @RequestParam String email,
                                @RequestParam(required = false) String dateOfBirth,
                                @RequestParam(required = false) String address,
                                @RequestParam(required = false) String contact,
                                @RequestParam(name = "image", required = false) MultipartFile image,
                                Model model) {
        try {
            Student student = studentService.getActiveStudentById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid or inactive student Id: " + id));

            // Email validation
            if (!student.getEmail().equals(email) && studentService.emailExists(email)) {
                throw new IllegalArgumentException("Email already exists.");
            }

            student.setName(name);
            student.setEmail(email);

            // Image Upload Handling
            if (image != null && !image.isEmpty()) {
                try {
                    String uploadsDir = new File("uploads").getAbsolutePath();
                    Files.createDirectories(Paths.get(uploadsDir));

                    // Generate a unique filename
                    String imageFilename = UUID.randomUUID() + "_" + image.getOriginalFilename();
                    Path filePath = Paths.get(uploadsDir, imageFilename);
                    Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                    // Delete old image if not default
                    if (student.getImageFilename() != null && !student.getImageFilename().equals("default_user.png")) {
                        Path oldImagePath = Paths.get(uploadsDir, student.getImageFilename());
                        Files.deleteIfExists(oldImagePath);
                    }

                    student.setImageFilename(imageFilename);
                } catch (IOException e) {
                    model.addAttribute("error", "Failed to upload the image.");
                    model.addAttribute("student", student);
                    return "students/edit";
                }
            }

            // Date parsing
            if (dateOfBirth != null && !dateOfBirth.isEmpty()) {
                try {
                    student.setDateOfBirth(LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                } catch (DateTimeParseException e) {
                    model.addAttribute("error", "Invalid date format. Please use yyyy-MM-dd.");
                    model.addAttribute("student", student);
                    return "students/edit";
                }
            }

            student.setAddress(address);
            student.setContact(contact);

            studentService.saveStudent(student);
            return "redirect:/students/" + id;
        } catch (IllegalArgumentException e) {
            model.addAttribute("currentPage", "students");
            model.addAttribute("error", e.getMessage());
            model.addAttribute("student", studentService.getActiveStudentById(id).orElse(new Student()));
            return "students/edit";
        }
    }


    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.softDeleteStudent(id);
        return "redirect:/students";
    }

    @GetMapping("/{studentId}/restore")
    public String restoreStudent(@PathVariable Long studentId) {
        studentService.restoreStudent(studentId);
        return "redirect:/students/" + studentId;
    }

    @GetMapping("/{studentId}")
    public String showStudentDetails(@PathVariable Long studentId,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     @RequestParam(defaultValue = "registrationDate,desc") String[] sort,
                                     Model model) {
        model.addAttribute("currentPage", "students");
        Student student = studentService.getActiveStudentById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Active student not found"));
        StudentDTO studentDTO = studentMapper.toDTO(student);
        model.addAttribute("student", studentDTO);

        Sort.Direction direction = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        Page<Registration> registrationsPage = registrationService.getRegistrationsByStudent(student, pageable);
        model.addAttribute("registrations", registrationsPage.getContent());
        model.addAttribute("currentPageNumber", page);
        model.addAttribute("totalPages", registrationsPage.getTotalPages());
        model.addAttribute("totalItems", registrationsPage.getTotalElements());
        model.addAttribute("sortField", sort[0]);
        model.addAttribute("sortDir", sort[1]);

        return "students/details";
    }

    @GetMapping("/{studentId}/register")
    public String showRegisterForm(@PathVariable Long studentId, Model model) {
        model.addAttribute("currentPage", "students");
        Student student = studentService.getActiveStudentById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Active student not found"));
        StudentDTO studentDTO = studentMapper.toDTO(student);
        model.addAttribute("student", studentDTO);

        // Get all active subjects
        List<Subject> allSubjects = subjectService.getAllActiveSubjects();

        // Get subjects the student is already registered for, filtered to active subjects
        List<Registration> registrations = registrationService.getRegistrationsByStudent(student);
        List<Subject> registeredSubjects = registrations.stream()
                .map(Registration::getSubject)
                .filter(Subject::isActive)
                .collect(Collectors.toList());

        // Filter out the subjects that the student has already registered
        List<Subject> unassignedSubjects = allSubjects.stream()
                .filter(subject -> !registeredSubjects.contains(subject))
                .collect(Collectors.toList());

        model.addAttribute("subjects", unassignedSubjects);
        return "students/register";
    }

    @PostMapping("/{studentId}/register")
    public String registerForSubject(@PathVariable Long studentId, @RequestParam Long subjectId) {
        Student student = studentService.getActiveStudentById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Active student not found"));
        Subject subject = subjectService.getActiveSubjectById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("Active subject not found"));

        Registration registration = new Registration();
        registration.setStudent(student);
        registration.setSubject(subject);

        registrationService.saveRegistration(registration);

        return "redirect:/students/" + studentId;
    }

    @PostMapping("/updateRegistration/{registrationId}")
    @ResponseBody
    public Map<String, Object> updateRegistration(@PathVariable Long registrationId, @RequestBody Map<String, Object> updates) {
        Map<String, Object> response = new HashMap<>();
        try {
            Registration registration = registrationService.getRegistrationById(registrationId)
                    .orElseThrow(() -> new IllegalArgumentException("Registration not found"));

            // Only allow updates if the status is 'Enrolling'
            if (registration.getStatus() == 1) {
                if (updates.containsKey("status")) {
                    int newStatus = (Integer) updates.get("status");
                    // Ensure we're not reverting to 'Enrolling' if the student already moved to another status
                    if (newStatus != 1 || registration.getStatus() == 1) {
                        // Validation: Cannot set status to 'Finished' (2) without a grade, perfection
                        if (newStatus == 2 && (!updates.containsKey("grade") || updates.get("grade") == null)) {
                            throw new IllegalArgumentException("A grade is required when setting status to 'Finished'.");
                        }
                        registration.setStatus(newStatus);
                    } else {
                        throw new IllegalArgumentException("Cannot revert status to 'Enrolling' once changed.");
                    }
                }
                if (updates.containsKey("grade")) {
                    registration.setGrade(updates.get("grade") != null ? Double.parseDouble(updates.get("grade").toString()) : null);
                }
                registrationService.updateRegistrationNoDuplicateCheck(registration); // This method is for updating
                response.put("success", true);
            } else {
                throw new IllegalArgumentException("Cannot update registration once it's not 'Enrolling'.");
            }
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "An unexpected error occurred: " + e.getMessage());
        }
        return response;
    }
}