package QuanLySinhVien.example.QuanLySinhVien.Repositories;

import QuanLySinhVien.example.QuanLySinhVien.Entities.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    // Find subjects by status
    List<Subject> findByStatus(Integer status);

    // Find an active subject by id
    Optional<Subject> findByIdAndStatus(Long id, Integer status);
}