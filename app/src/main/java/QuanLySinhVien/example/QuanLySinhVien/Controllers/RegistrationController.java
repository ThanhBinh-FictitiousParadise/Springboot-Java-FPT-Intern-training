package QuanLySinhVien.example.QuanLySinhVien.Controllers;

import QuanLySinhVien.example.QuanLySinhVien.DTO.RegistrationDTO;
import QuanLySinhVien.example.QuanLySinhVien.Entities.Registration;
import QuanLySinhVien.example.QuanLySinhVien.Entities.Student;
import QuanLySinhVien.example.QuanLySinhVien.Entities.Subject;
import QuanLySinhVien.example.QuanLySinhVien.Mapper.RegistrationMapper;
import QuanLySinhVien.example.QuanLySinhVien.Services.RegistrationService;
import QuanLySinhVien.example.QuanLySinhVien.Services.StudentService;
import QuanLySinhVien.example.QuanLySinhVien.Services.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private RegistrationMapper registrationMapper;

    @GetMapping("/new")
    public String showNewRegistrationForm(@RequestParam(required = false) Long studentId,
                                          @RequestParam(required = false) Long subjectId,
                                          Model model) {
        RegistrationDTO registrationDTO = new RegistrationDTO();
        model.addAttribute("registration", registrationDTO);
        model.addAttribute("currentPage", "registration");

        if (studentId != null && studentId != 0) {
            Optional student = studentService.getActiveStudentById(studentId);
            student.ifPresent(s -> model.addAttribute("student", s));
        }

        if (subjectId != null && subjectId != 0) {
            Optional subject = subjectService.getActiveSubjectById(subjectId);
            subject.ifPresent(s -> model.addAttribute("subject", s));
        }

        // Provide lists for dropdowns
        model.addAttribute("allStudents", studentService.getAllActiveStudents());
        model.addAttribute("allSubjects", subjectService.getAllActiveSubjects());

        return "registration/newRegistration";
    }

    @PostMapping("/new")
    public String register(@ModelAttribute("registration") RegistrationDTO dto, Model model, RedirectAttributes redirectAttributes) {
        try {
            // Convert DTO to Entity
            Registration registration = registrationMapper.toEntity(dto);

            // Check if studentId is null or empty
            if (registration.getStudent() == null || registration.getStudent().getId() == null) {
                throw new IllegalArgumentException("Student ID must not be null.");
            }

            registrationService.saveRegistration(registration);
            return "redirect:/students/" + dto.getStudentId();
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/registration/new?studentId=" + dto.getStudentId() + "&subjectId=" + dto.getSubjectId();
        }
    }
}