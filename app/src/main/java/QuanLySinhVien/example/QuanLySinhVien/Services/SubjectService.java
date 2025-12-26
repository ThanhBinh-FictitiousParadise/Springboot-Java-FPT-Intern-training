package QuanLySinhVien.example.QuanLySinhVien.Services;

import QuanLySinhVien.example.QuanLySinhVien.Entities.Subject;
import QuanLySinhVien.example.QuanLySinhVien.Repositories.SubjectRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {

    private static final Logger logger = LoggerFactory.getLogger(SubjectService.class);

    @Autowired
    private SubjectRepository subjectRepository;

    @PostConstruct
    public void init() {
        logger.info("SubjectService: Initializing...");
    }

    @PreDestroy
    public void cleanup() {
        logger.info("SubjectService: Cleaning up...");
    }

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public List<Subject> getAllActiveSubjects() {
        return subjectRepository.findByStatus(1);
    }

    public Optional<Subject> getSubjectById(Long id) {
        return subjectRepository.findById(id);
    }

    public Optional<Subject> getActiveSubjectById(Long id) {
        return subjectRepository.findByIdAndStatus(id, 1);
    }

    public Subject saveSubject(Subject subject) {
        if (subject.getStatus() == null) {
            subject.setStatus(1);
        }
        return subjectRepository.save(subject);
    }

    public void updateSubject(Long id, Subject subject) {
        Optional<Subject> existingSubject = subjectRepository.findById(id);
        if (existingSubject.isPresent()) {
            Subject updatedSubject = existingSubject.get();
            updatedSubject.setName(subject.getName());
            updatedSubject.setDescription(subject.getDescription());
            updatedSubject.setCredits(subject.getCredits()); // New line
            updatedSubject.setStatus(1);
            subjectRepository.save(updatedSubject);
        } else {
            throw new IllegalArgumentException("Subject not found.");
        }
    }

    public void deleteSubject(Long id) {
        Optional<Subject> subject = subjectRepository.findById(id);
        if (subject.isPresent()) {
            Subject subjectToDelete = subject.get();
            subjectToDelete.setStatus(0);
            subjectRepository.save(subjectToDelete);
        }
    }

    public void restoreSubject(Long id) {
        Optional<Subject> subject = subjectRepository.findById(id);
        if (subject.isPresent()) {
            Subject subjectToRestore = subject.get();
            subjectToRestore.setStatus(1);
            subjectRepository.save(subjectToRestore);
        }
    }
}