package edu.unimagdalena.lms.services.mapper;

import edu.unimagdalena.lms.api.dto.LessonDtos.*;
import edu.unimagdalena.lms.entities.Course;
import edu.unimagdalena.lms.entities.Lesson;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class LessonMapperTest {
    private final LessonMapper mapper = Mappers.getMapper(LessonMapper.class);

    @Test
    void toEntity_shouldMapCreate() {
        Lesson lesson = mapper.toEntity(new LessonCreate(10L, "Intro", 1));

        assertThat(lesson.getId()).isNull();
        assertThat(lesson.getTitle()).isEqualTo("Intro");
        assertThat(lesson.getOrderIndex()).isEqualTo(1);
        assertThat(lesson.getCourse()).isNotNull();
        assertThat(lesson.getCourse().getId()).isEqualTo(10L);
    }

    @Test
    void toResponse_shouldMapEntity() {
        Lesson lesson = Lesson.builder()
                .id(5L)
                .course(Course.builder().id(9L).build())
                .title("Lesson A")
                .orderIndex(2)
                .build();

        LessonResponse dto = mapper.toResponse(lesson);

        assertThat(dto.id()).isEqualTo(5L);
        assertThat(dto.courseId()).isEqualTo(9L);
        assertThat(dto.title()).isEqualTo("Lesson A");
        assertThat(dto.orderIndex()).isEqualTo(2);
    }

    @Test
    void patch_shouldIgnoreNullsAndNotTouchCourse() {
        Course course = Course.builder().id(1L).build();
        Lesson lesson = Lesson.builder()
                .id(1L)
                .course(course)
                .title("Old title")
                .orderIndex(1)
                .build();

        mapper.patch(lesson, new LessonUpdate(null, 99));

        assertThat(lesson.getCourse()).isSameAs(course);
        assertThat(lesson.getTitle()).isEqualTo("Old title");
        assertThat(lesson.getOrderIndex()).isEqualTo(99);
    }
}

