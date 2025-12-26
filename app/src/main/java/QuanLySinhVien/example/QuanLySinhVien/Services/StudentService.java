package QuanLySinhVien.example.QuanLySinhVien.Services;

import QuanLySinhVien.example.QuanLySinhVien.Entities.Student;
import QuanLySinhVien.example.QuanLySinhVien.Repositories.StudentRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @PostConstruct
    public void init() {
        logger.info("StudentService: Initializing...");
    }

    @PreDestroy
    public void cleanup() {
        logger.info("StudentService: Cleaning up...");
    }

    public List<Student> getAllActiveStudents() {
        return studentRepository.findByStatus(1);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getActiveStudentById(Long id) {
        return studentRepository.findByIdAndStatus(id, 1);
    }

    public String generateNextSID() {
        int currentYear = LocalDate.now().getYear();
        String prefix = "SID" + currentYear;

        // Find the highest SID for the current year
        List<Student> students = studentRepository.findAllBySidStartingWith(prefix);
        int maxSequence = students.stream()
                .map(s -> s.getSid().substring(7)) // This remove the SID and Year part
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(0);
        return prefix + String.format("%02d", maxSequence + 1);
    }

    public Student saveStudent(Student student) {
        if (student.getId() == null) {
            if (studentRepository.existsByEmail(student.getEmail())) {
                throw new IllegalArgumentException("Email already exists.");
            }
        } else {
            Optional<Student> existingStudent = studentRepository.findById(student.getId());
            if (existingStudent.isPresent()) {
                Student existing = existingStudent.get();
                if (!existing.getEmail().equals(student.getEmail()) && studentRepository.existsByEmail(student.getEmail())) {
                    throw new IllegalArgumentException("Email already exists.");
                }
            } else {
                throw new IllegalArgumentException("Student not found.");
            }
        }
        return studentRepository.save(student);
    }

    public void softDeleteStudent(Long id) {
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            student.setStatus(0); // Soft delete
            studentRepository.save(student);
        }
    }

    public void restoreStudent(Long id) {
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            student.setStatus(1); // Restore
            studentRepository.save(student);
        }
    }

    public boolean emailExists(String email) {
        return studentRepository.existsByEmail(email);
    }
}