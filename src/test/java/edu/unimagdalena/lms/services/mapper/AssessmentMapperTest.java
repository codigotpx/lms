package edu.unimagdalena.lms.services.mapper;

import edu.unimagdalena.lms.api.dto.AssessmentDtos.*;
import edu.unimagdalena.lms.entities.Assessment;
import edu.unimagdalena.lms.entities.Course;
import edu.unimagdalena.lms.entities.Student;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AssessmentMapperTest {
    private final AssessmentMapper mapper = Mappers.getMapper(AssessmentMapper.class);

    @Test
    void toEntity_shouldMapCreate() {
        Instant takenAt = Instant.parse("2026-03-01T10:15:30Z");
        Assessment assessment = mapper.toEntity(new AssessmentRequestCreate(10L, 20L, "QUIZ", 95, takenAt));

        assertThat(assessment.getId()).isNull();
        assertThat(assessment.getType()).isEqualTo("QUIZ");
        assertThat(assessment.getScore()).isEqualTo(95);
        assertThat(assessment.getTakenAt()).isEqualTo(takenAt);
        assertThat(assessment.getStudent()).isNotNull();
        assertThat(assessment.getStudent().getId()).isEqualTo(10L);
        assertThat(assessment.getCourse()).isNotNull();
        assertThat(assessment.getCourse().getId()).isEqualTo(20L);
    }

    @Test
    void toResponse_shouldMapEntity() {
        Instant takenAt = Instant.parse("2026-03-02T10:15:30Z");
        Assessment assessment = Assessment.builder()
                .id(5L)
                .student(Student.builder().id(11L).build())
                .course(Course.builder().id(22L).build())
                .type("EXAM")
                .score(80)
                .takenAt(takenAt)
                .build();

        AssessmentResponse dto = mapper.toResponse(assessment);

        assertThat(dto.id()).isEqualTo(5L);
        assertThat(dto.studentId()).isEqualTo(11L);
        assertThat(dto.courseId()).isEqualTo(22L);
        assertThat(dto.type()).isEqualTo("EXAM");
        assertThat(dto.score()).isEqualTo(80);
        assertThat(dto.takenAt()).isEqualTo(takenAt);
    }

    @Test
    void patch_shouldIgnoreNullsAndNotTouchStudentOrCourse() {
        Student student = Student.builder().id(1L).build();
        Course course = Course.builder().id(2L).build();

        Assessment assessment = Assessment.builder()
                .id(100L)
                .student(student)
                .course(course)
                .type("OLD")
                .score(10)
                .takenAt(Instant.parse("2026-03-01T00:00:00Z"))
                .build();

        mapper.patch(assessment, new AssessmentRequestUpdate(null, 77, null));

        assertThat(assessment.getStudent()).isSameAs(student);
        assertThat(assessment.getCourse()).isSameAs(course);
        assertThat(assessment.getType()).isEqualTo("OLD");
        assertThat(assessment.getScore()).isEqualTo(77);
        assertThat(assessment.getTakenAt()).isNotNull();
    }
}

