package QuanLySinhVien.example.QuanLySinhVien.Mapper;

import QuanLySinhVien.example.QuanLySinhVien.DTO.StudentDTO;
import QuanLySinhVien.example.QuanLySinhVien.Entities.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "sid", target = "sid")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "dateOfBirth", target = "dateOfBirth")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "contact", target = "contact")
    @Mapping(source = "imageFilename", target = "imageFilename")
    StudentDTO toDTO(Student student);

    List<StudentDTO> toDTOList(List<Student> students);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "sid", target = "sid")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "dateOfBirth", target = "dateOfBirth")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "contact", target = "contact")
    @Mapping(source = "imageFilename", target = "imageFilename")
    Student toEntity(StudentDTO studentDTO);
}