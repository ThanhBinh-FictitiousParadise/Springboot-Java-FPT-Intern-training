package QuanLySinhVien.example.QuanLySinhVien.Controllers;

import QuanLySinhVien.example.QuanLySinhVien.Entities.Subject;
import QuanLySinhVien.example.QuanLySinhVien.Repositories.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {

    @Autowired
    private RegistrationRepository registrationRepository;

    @GetMapping("/")
    public String dashboard(Model model) {
        // Fetch the latest data
        List<Object[]> mostRegisteredSubjects = registrationRepository.findSubjectsWithMostRegistrations();
        List<Map<String, Object>> topSubjects = new ArrayList<>();
        System.out.println("Dashboard data: " + mostRegisteredSubjects);

        // Limit to top 5 active subjects
        for (int i = 0; i < Math.min(5, mostRegisteredSubjects.size()); i++) {
            Object[] entry = mostRegisteredSubjects.get(i);
            Subject subject = (Subject) entry[0];
            if (subject.isActive()) {
                Long studentCount = (Long) entry[1];
                Map<String, Object> subjectMap = new HashMap<>();
                subjectMap.put("name", subject.getName());
                subjectMap.put("count", studentCount);
                topSubjects.add(subjectMap);
            }
        }

        model.addAttribute("topSubjects", topSubjects);
        model.addAttribute("currentPage", "dashboard");
        return "dashboard/index";
    }

    @ControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(IllegalArgumentException.class)
        public ModelAndView handleIllegalArgumentException(IllegalArgumentException e, Model model) {
            model.addAttribute("currentPage", "error");
            ModelAndView mav = new ModelAndView("error-page");
            mav.addObject("errorMessage", e.getMessage());
            return mav;
        }
    }

    @ControllerAdvice
    public class GlobalModelAttributes {

        @ModelAttribute
        public void addGlobalAttributes(Model model) {
            if (!model.containsAttribute("currentPage")) {
                model.addAttribute("currentPage", "");  // Default empty value
            }
        }
    }

}