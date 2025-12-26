package QuanLySinhVien.example.QuanLySinhVien.Repositories;

import QuanLySinhVien.example.QuanLySinhVien.Entities.Registration;
import QuanLySinhVien.example.QuanLySinhVien.Entities.Student;
import QuanLySinhVien.example.QuanLySinhVien.Entities.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    List<Registration> findByStudent(Student student);

    List<Registration> findBySubject(Subject subject);

    Page<Registration> findByStudent(Student student, Pageable pageable);

    Page<Registration> findBySubject(Subject subject, Pageable pageable);

    @Query("SELECT s, COUNT(r.student.id) FROM Subject s " +
            "JOIN s.registrations r " +
            "WHERE s.status = 1 " +
            "AND r.student.status = 1 " +
            "GROUP BY s.id " +
            "ORDER BY COUNT(r.student.id) DESC")
    List<Object[]> findSubjectsWithMostRegistrations();

    @Query("SELECT r FROM Registration r WHERE r.student = :student AND r.semester = :semester AND r.academicYear = :academicYear")
    List<Registration> findByStudentAndSemesterAndYear(Student student, String semester, Integer academicYear);

    @Query("SELECT r FROM Registration r WHERE r.student = :student AND r.subject = :subject AND r.semester = :semester AND r.academicYear = :academicYear")
    Optional<Registration> findByStudentAndSubjectAndSemesterAndAcademicYear(Student student, Subject subject, String semester, Integer academicYear);
}