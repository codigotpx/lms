package edu.unimagdalena.lms.services.mapper;

import edu.unimagdalena.lms.api.dto.InstructorDtos.*;
import edu.unimagdalena.lms.api.dto.InstructorProfileDtos;
import edu.unimagdalena.lms.entities.Instructor;
import org.mapstruct.*;

@Mapper()
public interface InstructorMapper {
    @Mapping(target = "instructorProfile", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "courses", ignore = true)
    Instructor toEntity(InstructorRequestCreate dto);

    InstructorResponse toResponse(Instructor entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "instructorProfile", ignore = true)
    void patch(@MappingTarget Instructor target, InstructorRequestUpdate changes);

}
