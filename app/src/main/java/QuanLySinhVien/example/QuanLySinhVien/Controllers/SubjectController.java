package QuanLySinhVien.example.QuanLySinhVien.Controllers;

import QuanLySinhVien.example.QuanLySinhVien.DTO.SubjectDTO;
import QuanLySinhVien.example.QuanLySinhVien.Entities.Registration;
import QuanLySinhVien.example.QuanLySinhVien.Entities.Student;
import QuanLySinhVien.example.QuanLySinhVien.Entities.Subject;
import QuanLySinhVien.example.QuanLySinhVien.Mapper.SubjectMapper;
import QuanLySinhVien.example.QuanLySinhVien.Services.RegistrationService;
import QuanLySinhVien.example.QuanLySinhVien.Services.StudentService;
import QuanLySinhVien.example.QuanLySinhVien.Services.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/subjects")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private SubjectMapper subjectMapper;

    @GetMapping
    public String showSubjects(Model model) {
        List<SubjectDTO> subjectDTOs = subjectMapper.toDTOList(subjectService.getAllSubjects());
        model.addAttribute("currentPage", "subjects");
        model.addAttribute("subjects", subjectDTOs);
        return "subjects/list";
    }

    @GetMapping("/add")
    public String addSubjectForm(Model model) {
        model.addAttribute("currentPage", "subjects");
        model.addAttribute("subject", new Subject());
        return "subjects/add";
    }

    @PostMapping("/add")
    public String addSubject(@ModelAttribute("subject") Subject subject) {
        subject.setStatus(1);
        subjectService.saveSubject(subject);
        return "redirect:/subjects";
    }

    @GetMapping("/{subjectId}")
    public String showSubjectDetails(@PathVariable Long subjectId,
                                     @RequestParam(defaultValue = "0") int page,
                                     Model model) {
        model.addAttribute("currentPage", "subjects");
        Subject subject = subjectService.getActiveSubjectById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("Subject not found"));
        SubjectDTO subjectDTO = subjectMapper.toDTO(subject);
        model.addAttribute("subject", subjectDTO);

        Page<Registration> registrationsPage = registrationService.getRegistrationsBySubject(subject, page);
        List<Map<String, Object>> studentData = new ArrayList<>();
        for (Registration registration : registrationsPage.getContent()) {
            Student student = registration.getStudent();
            if (student.isActive()) {
                Map<String, Object> studentMap = new HashMap<>();
                studentMap.put("studentId", student.getSid());
                studentMap.put("Id", student.getId());
                studentMap.put("studentName", student.getName());
                studentMap.put("email", student.getEmail());
                studentData.add(studentMap);
            }
        }

        model.addAttribute("studentData", studentData);
        model.addAttribute("currentPageNumber", page);
        model.addAttribute("totalPages", registrationsPage.getTotalPages());
        model.addAttribute("totalItems", registrationsPage.getTotalElements());

        return "subjects/details";
    }

    @GetMapping("/{subjectId}/edit")
    public String editSubjectForm(@PathVariable Long subjectId, Model model) {
        model.addAttribute("currentPage", "subjects");
        Subject subject = subjectService.getActiveSubjectById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("Subject not found"));
        SubjectDTO subjectDTO = subjectMapper.toDTO(subject);
        model.addAttribute("subject", subjectDTO);
        return "subjects/edit";
    }

    @PostMapping("/{subjectId}/edit")
    public String updateSubject(@PathVariable Long subjectId, @ModelAttribute("subject") Subject subject) {
        subject.setStatus(1);
        subjectService.updateSubject(subjectId, subject);
        return "redirect:/subjects";
    }

    @GetMapping("/{subjectId}/delete")
    public String deleteSubject(@PathVariable Long subjectId) {
        subjectService.deleteSubject(subjectId);
        return "redirect:/subjects";
    }

    @GetMapping("/{subjectId}/register")
    public String showRegisterStudents(@PathVariable Long subjectId, Model model,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        model.addAttribute("currentPage", "subjects");

        Subject subject = subjectService.getActiveSubjectById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("Subject not found"));
        SubjectDTO subjectDTO = subjectMapper.toDTO(subject);
        model.addAttribute("subject", subjectDTO);

        List<Student> allStudents = studentService.getAllActiveStudents();

        int start = page * size;
        List<Student> students = allStudents.subList(
                Math.min(start, allStudents.size()),
                Math.min(start + size, allStudents.size())
        );
        model.addAttribute("students", students);

        List<Registration> registrations = (List<Registration>) registrationService.getRegistrationsBySubject(subject);
        List<Student> registeredStudents = registrations.stream()
                .map(Registration::getStudent)
                .filter(Student::isActive)
                .collect(Collectors.toList());
        model.addAttribute("registeredStudents", registeredStudents);

        model.addAttribute("currentPageNumber", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) allStudents.size() / size));
        model.addAttribute("totalItems", allStudents.size());

        return "subjects/registerStudents";
    }

    @PostMapping("/{subjectId}/register")
    public String registerStudentForSubject(@PathVariable Long subjectId, @RequestParam Long studentId) {
        Subject subject = subjectService.getActiveSubjectById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("Subject not found"));
        Student student = studentService.getActiveStudentById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Active student not found"));

        Registration registration = new Registration();
        registration.setStudent(student);
        registration.setSubject(subject);

        registrationService.saveRegistration(registration);

        return "redirect:/subjects/" + subjectId;
    }

    @GetMapping("/{subjectId}/restore")
    public String restoreSubject(@PathVariable Long subjectId) {
        subjectService.restoreSubject(subjectId);
        return "redirect:/subjects/" + subjectId;
    }

}