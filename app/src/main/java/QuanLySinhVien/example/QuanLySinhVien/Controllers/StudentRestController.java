package QuanLySinhVien.example.QuanLySinhVien.Controllers;

import QuanLySinhVien.example.QuanLySinhVien.DTO.StudentDTO;
import QuanLySinhVien.example.QuanLySinhVien.Mapper.StudentMapper;
import QuanLySinhVien.example.QuanLySinhVien.Services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentRestController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentMapper studentMapper;

    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        return ResponseEntity.ok(studentMapper.toDTOList(studentService.getAllActiveStudents()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
        return studentService.getActiveStudentById(id)
                .map(student -> ResponseEntity.ok(studentMapper.toDTO(student)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<StudentDTO> addStudent(@RequestBody StudentDTO studentDTO) {
        StudentDTO savedStudent = studentMapper.toDTO(studentService.saveStudent(studentMapper.toEntity(studentDTO)));
        return ResponseEntity.ok(savedStudent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable Long id, @RequestBody StudentDTO studentDTO) {
        if (!studentService.getActiveStudentById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        studentDTO.setId(id); // Ensure the ID matches for update
        StudentDTO updatedStudent = studentMapper.toDTO(studentService.saveStudent(studentMapper.toEntity(studentDTO)));
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        if (studentService.getActiveStudentById(id).isPresent()) {
            studentService.softDeleteStudent(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}