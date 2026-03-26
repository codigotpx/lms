package edu.unimagdalena.lms.services.mapper;

import edu.unimagdalena.lms.entities.Student;
import edu.unimagdalena.lms.api.dto.StudentDtos.*;
import org.mapstruct.*;

@Mapper()
public interface StudentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "assessments", ignore = true)
    @Mapping(target = "enrollments", ignore = true)
    Student toEntity(StudentCreate dto);

    StudentResponse toResponse(Student entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patch(@MappingTarget Student target, StudentUpdate changes);

}