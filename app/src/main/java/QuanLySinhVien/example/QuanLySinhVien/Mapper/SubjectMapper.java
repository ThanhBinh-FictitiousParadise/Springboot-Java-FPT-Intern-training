package QuanLySinhVien.example.QuanLySinhVien.Mapper;

import QuanLySinhVien.example.QuanLySinhVien.DTO.SubjectDTO;
import QuanLySinhVien.example.QuanLySinhVien.Entities.Subject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubjectMapper {

    SubjectMapper INSTANCE = Mappers.getMapper(SubjectMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "credits", target = "credits")
    @Mapping(source = "status", target = "status")
    SubjectDTO toDTO(Subject subject);

    List<SubjectDTO> toDTOList(List<Subject> subjects); // Fixed generics

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "credits", target = "credits")
    @Mapping(source = "status", target = "status")
    Subject toEntity(SubjectDTO subjectDTO);
}
