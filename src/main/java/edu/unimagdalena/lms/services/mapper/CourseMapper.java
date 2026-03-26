package edu.unimagdalena.lms.services.mapper;

import edu.unimagdalena.lms.api.dto.CourseDtos.*;
import edu.unimagdalena.lms.entities.Course;
import org.mapstruct.*;

@Mapper()
public interface CourseMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "assessments", ignore = true)
    @Mapping(target = "lessons", ignore = true)
    @Mapping(target = "enrollments", ignore = true)
    @Mapping(target = "instructor.id", source = "instructorId")
    Course toEntity(CourseRequestCreate dto);

    @Mapping(target = "instructorId", source = "instructor.id")
    CourseResponse toResponse(Course entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "instructor", ignore = true)
    void patch(@MappingTarget Course target, CourseRequestUpdate changes);
}
