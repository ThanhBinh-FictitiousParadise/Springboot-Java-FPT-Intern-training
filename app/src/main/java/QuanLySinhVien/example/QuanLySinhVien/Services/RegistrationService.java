package QuanLySinhVien.example.QuanLySinhVien.Services;

import QuanLySinhVien.example.QuanLySinhVien.Entities.Registration;
import QuanLySinhVien.example.QuanLySinhVien.Entities.Student;
import QuanLySinhVien.example.QuanLySinhVien.Entities.Subject;
import QuanLySinhVien.example.QuanLySinhVien.Repositories.RegistrationRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationService.class);

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private StudentService studentService;

    @Autowired
    private SubjectService subjectService;

    @PostConstruct
    public void init() {
        logger.info("RegistrationService: Initializing...");
    }

    @PreDestroy
    public void cleanup() {
        logger.info("RegistrationService: Cleaning up...");
    }

    private boolean isDuplicateRegistration(Registration registration) {
        return registrationRepository.findByStudentAndSubjectAndSemesterAndAcademicYear(
                registration.getStudent(),
                registration.getSubject(),
                registration.getSemester(),
                registration.getAcademicYear()
        ).isPresent();
    }

    public Registration saveRegistration(Registration registration) {
        if (isDuplicateRegistration(registration)) {
            throw new IllegalArgumentException("This student is already registered for this subject in the specified semester and academic year.");
        }

        if (!studentService.getActiveStudentById(registration.getStudent().getId()).isPresent()) {
            throw new IllegalArgumentException("Cannot register a deleted student.");
        }
        if (!subjectService.getActiveSubjectById(registration.getSubject().getId()).isPresent()) {
            throw new IllegalArgumentException("Cannot register for a deleted subject.");
        }

        if (registration.getStatus() == null) {
            registration.setStatus(1); // Default to Enrolling
        }

        registration.setRegistrationDate(LocalDateTime.now());
        return registrationRepository.save(registration);
    }

    public Page<Registration> getRegistrationsByStudent(Student student, Pageable pageable) {
        return registrationRepository.findByStudent(student, pageable);
    }

    public Page getRegistrationsBySubject(Subject subject, int page) {
        Pageable pageable = PageRequest.of(page, 5, Sort.by("registrationDate").descending());
        return registrationRepository.findBySubject(subject, pageable);
    }

    public List getRegistrationsByStudent(Student student) {
        return registrationRepository.findByStudent(student).stream()
                .filter(Registration::isActive)
                .filter(reg -> subjectService.getActiveSubjectById(reg.getSubject().getId()).isPresent())
                .collect(Collectors.toList());
    }

    public List getRegistrationsBySubject(Subject subject) {
        return registrationRepository.findBySubject(subject).stream()
                .filter(Registration::isActive)
                .filter(reg -> studentService.getActiveStudentById(reg.getStudent().getId()).isPresent())
                .collect(Collectors.toList());
    }

    public Optional<Registration> getRegistrationById(Long registrationId) {
        return registrationRepository.findById(registrationId);
    }

    public Iterable getAllRegistrations() {
        return registrationRepository.findAll();
    }

    public void deleteRegistration(Long registrationId) {
        registrationRepository.deleteById(registrationId);
    }

    public Registration updateRegistrationNoDuplicateCheck(Registration registration) {
        return registrationRepository.save(registration);
    }
}