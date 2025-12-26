package QuanLySinhVien.example.QuanLySinhVien.Mapper;

import QuanLySinhVien.example.QuanLySinhVien.DTO.RegistrationDTO;
import QuanLySinhVien.example.QuanLySinhVien.Entities.Registration;
import QuanLySinhVien.example.QuanLySinhVien.Entities.Student;
import QuanLySinhVien.example.QuanLySinhVien.Entities.Subject;
import QuanLySinhVien.example.QuanLySinhVien.Services.StudentService;
import QuanLySinhVien.example.QuanLySinhVien.Services.SubjectService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class RegistrationMapper {

    @Autowired
    protected StudentService studentService;

    @Autowired
    protected SubjectService subjectService;

    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "subject.id", target = "subjectId")
    public abstract RegistrationDTO toDTO(Registration registration);

    public abstract List<RegistrationDTO> toDTOList(List<Registration> registrations);

    @Mapping(target = "student", source = "studentId", qualifiedByName = "mapToStudent")
    @Mapping(target = "subject", source = "subjectId", qualifiedByName = "mapToSubject")
    public abstract Registration toEntity(RegistrationDTO registrationDTO);

    @Named("mapToStudent")
    protected Student mapToStudent(Long studentId) {
        if (studentId == null) {
            return null;
        }
        return studentService.getActiveStudentById(studentId).orElse(null);
    }

    @Named("mapToSubject")
    protected Subject mapToSubject(Long subjectId) {
        if (subjectId == null) {
            return null;
        }
        return subjectService.getActiveSubjectById(subjectId).orElse(null);
    }

}
