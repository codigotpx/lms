package edu.unimagdalena.lms.services.mapper;

import edu.unimagdalena.lms.api.dto.InstructorProfileDtos.*;
import edu.unimagdalena.lms.entities.InstructorProfile;
import org.mapstruct.*;

@Mapper()
public interface InstructorProfileMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "instructor", ignore = true)
    InstructorProfile toEntity(InstructorProfileRequestCreate dto);

    @Mapping(target = "instructorId", source = "instructor.id")
    InstructorProfileResponse toResponse(InstructorProfile entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patch(@MappingTarget InstructorProfile target, InstructorProfileRequestUpdate changes);
}
