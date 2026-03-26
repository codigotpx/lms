package edu.unimagdalena.lms.services.mapper;

import edu.unimagdalena.lms.api.dto.EnrollmentDtos.*;
import edu.unimagdalena.lms.entities.Course;
import edu.unimagdalena.lms.entities.Enrollment;
import edu.unimagdalena.lms.entities.Student;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class EnrollmentMapperTest {
    private final EnrollmentMapper mapper = Mappers.getMapper(EnrollmentMapper.class);

    @Test
    void toEntity_shouldMapCreate() {
        Enrollment enrollment = mapper.toEntity(new EnrollmentCreate(10L, 20L, "ACTIVE"));

        assertThat(enrollment.getId()).isNull();
        assertThat(enrollment.getStatus()).isEqualTo("ACTIVE");
        assertThat(enrollment.getEnrolledAt()).isNull();
        assertThat(enrollment.getStudent()).isNotNull();
        assertThat(enrollment.getStudent().getId()).isEqualTo(10L);
        assertThat(enrollment.getCourse()).isNotNull();
        assertThat(enrollment.getCourse().getId()).isEqualTo(20L);
    }

    @Test
    void toResponse_shouldMapEntity() {
        Instant enrolledAt = Instant.parse("2026-03-01T10:15:30Z");

        Enrollment enrollment = Enrollment.builder()
                .id(5L)
                .student(Student.builder().id(11L).build())
                .course(Course.builder().id(22L).build())
                .status("ACTIVE")
                .enrolledAt(enrolledAt)
                .build();

        EnrollmentResponse dto = mapper.toResponse(enrollment);

        assertThat(dto.id()).isEqualTo(5L);
        assertThat(dto.studentId()).isEqualTo(11L);
        assertThat(dto.courseId()).isEqualTo(22L);
        assertThat(dto.status()).isEqualTo("ACTIVE");
        assertThat(dto.enrolledAt()).isEqualTo(enrolledAt);
    }

    @Test
    void patch_shouldIgnoreNullsAndNotTouchStudentCourseOrEnrolledAt() {
        Instant enrolledAt = Instant.parse("2026-03-01T10:15:30Z");
        Student student = Student.builder().id(1L).build();
        Course course = Course.builder().id(2L).build();

        Enrollment enrollment = Enrollment.builder()
                .id(1L)
                .student(student)
                .course(course)
                .status("OLD")
                .enrolledAt(enrolledAt)
                .build();

        mapper.patch(enrollment, new EnrollmentUpdate(null));

        assertThat(enrollment.getStudent()).isSameAs(student);
        assertThat(enrollment.getCourse()).isSameAs(course);
        assertThat(enrollment.getEnrolledAt()).isEqualTo(enrolledAt);
        assertThat(enrollment.getStatus()).isEqualTo("OLD");
    }
}

