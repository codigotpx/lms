package edu.unimagdalena.lms.services.mapper;

import edu.unimagdalena.lms.api.dto.LessonDtos.*;
import edu.unimagdalena.lms.entities.Lesson;
import org.mapstruct.*;

@Mapper()
public interface LessonMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "course.id", source = "courseId")
    Lesson toEntity(LessonCreate dto);

    @Mapping(target = "courseId", source = "course.id")
    LessonResponse toResponse(Lesson entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "course", ignore = true)
    void patch(@MappingTarget Lesson target, LessonUpdate changes);
}

