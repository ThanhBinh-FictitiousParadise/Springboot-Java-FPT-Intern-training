package QuanLySinhVien.example.QuanLySinhVien.Repositories;

import QuanLySinhVien.example.QuanLySinhVien.Entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByStatus(Integer status);
    Optional<Student> findByIdAndStatus(Long id, Integer status);
    List<Student> findAllBySidStartingWith(String sidPrefix);
    boolean existsBySid(String sid);
    boolean existsByEmail(String email);
}