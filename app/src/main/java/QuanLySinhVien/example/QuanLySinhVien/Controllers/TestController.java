package QuanLySinhVien.example.QuanLySinhVien.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.jdbc.core.JdbcTemplate;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/check")
    public String checkDatabaseConnection() {
        try {
            // Checking da connection babey
            String result = jdbcTemplate.queryForObject("SELECT 1", String.class);
            return "Database connected successfully! Result: " + result;
        } catch (Exception e) {
            return "Error connecting to database: " + e.getMessage();
        }
    }
}

