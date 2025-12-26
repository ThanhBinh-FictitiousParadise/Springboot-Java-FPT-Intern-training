package QuanLySinhVien.example.QuanLySinhVien;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseTestRunner implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        // Test query
        String result = jdbcTemplate.queryForObject("SELECT 'Hello, World!'", String.class);
        System.out.println("Database Test Result: " + result);
    }
}

