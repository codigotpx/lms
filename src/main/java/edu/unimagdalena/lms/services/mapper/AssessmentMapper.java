package edu.unimagdalena.lms.services.mapper;

import edu.unimagdalena.lms.api.dto.AssessmentDtos.AssessmentResponse;
import edu.unimagdalena.lms.api.dto.AssessmentDtos.AssessmentRequestCreate;
import edu.unimagdalena.lms.api.dto.AssessmentDtos.AssessmentRequestUpdate;
import edu.unimagdalena.lms.entities.Assessment;
import org.mapstruct.*;

@Mapper()
public interface AssessmentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "student.id", source = "studentId")
    @Mapping(target = "course.id", source = "courseId")
    Assessment toEntity(AssessmentRequestCreate dto);

    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "courseId", source = "course.id")
    AssessmentResponse toResponse(Assessment entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "course", ignore = true)
    void patch(@MappingTarget Assessment target, AssessmentRequestUpdate changes);
}
