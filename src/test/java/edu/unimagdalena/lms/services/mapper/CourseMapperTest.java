package edu.unimagdalena.lms.services.mapper;

import edu.unimagdalena.lms.api.dto.CourseDtos.*;
import edu.unimagdalena.lms.entities.Course;
import edu.unimagdalena.lms.entities.Instructor;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CourseMapperTest {
    private final CourseMapper mapper = Mappers.getMapper(CourseMapper.class);

    @Test
    void toEntity_shouldMapCreate() {
        Course course = mapper.toEntity(new CourseRequestCreate(15L, "Algorithms", "DRAFT", true));

        assertThat(course.getId()).isNull();
        assertThat(course.getTitle()).isEqualTo("Algorithms");
        assertThat(course.getStatus()).isEqualTo("DRAFT");
        assertThat(course.getActive()).isEqualTo(true);
        assertThat(course.getInstructor()).isNotNull();
        assertThat(course.getInstructor().getId()).isEqualTo(15L);
    }

    @Test
    void toResponse_shouldMapEntity() {
        Instant createdAt = Instant.parse("2026-03-01T10:15:30Z");
        Instant updatedAt = Instant.parse("2026-03-02T10:15:30Z");

        Course course = Course.builder()
                .id(5L)
                .instructor(Instructor.builder().id(9L).build())
                .title("Databases")
                .status("PUBLISHED")
                .active(true)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        CourseResponse dto = mapper.toResponse(course);

        assertThat(dto.id()).isEqualTo(5L);
        assertThat(dto.instructorId()).isEqualTo(9L);
        assertThat(dto.title()).isEqualTo("Databases");
        assertThat(dto.status()).isEqualTo("PUBLISHED");
        assertThat(dto.active()).isEqualTo(true);
        assertThat(dto.createdAt()).isEqualTo(createdAt);
        assertThat(dto.updatedAt()).isEqualTo(updatedAt);
    }

    @Test
    void patch_shouldIgnoreNullsAndNotTouchInstructor() {
        Instructor instructor = Instructor.builder().id(1L).build();
        Course course = Course.builder()
                .id(1L)
                .instructor(instructor)
                .title("Old title")
                .status("OLD")
                .active(false)
                .build();

        mapper.patch(course, new CourseRequestUpdate(null, "NEW", true));

        assertThat(course.getTitle()).isEqualTo("Old title");
        assertThat(course.getStatus()).isEqualTo("NEW");
        assertThat(course.getActive()).isEqualTo(true);
        assertThat(course.getInstructor()).isSameAs(instructor);
        assertThat(course.getInstructor().getId()).isEqualTo(1L);
    }
}
