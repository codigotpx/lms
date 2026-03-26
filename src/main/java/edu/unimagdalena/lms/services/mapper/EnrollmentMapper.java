package edu.unimagdalena.lms.services.mapper;

import edu.unimagdalena.lms.api.dto.EnrollmentDtos.*;
import edu.unimagdalena.lms.entities.Enrollment;
import org.mapstruct.*;

@Mapper()
public interface EnrollmentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enrolledAt", ignore = true)
    @Mapping(target = "student.id", source = "studentId")
    @Mapping(target = "course.id", source = "courseId")
    Enrollment toEntity(EnrollmentCreate dto);

    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "courseId", source = "course.id")
    EnrollmentResponse toResponse(Enrollment entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "enrolledAt", ignore = true)
    void patch(@MappingTarget Enrollment target, EnrollmentUpdate changes);
}

