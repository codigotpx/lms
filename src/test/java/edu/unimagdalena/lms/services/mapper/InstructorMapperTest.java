package edu.unimagdalena.lms.services.mapper;

import edu.unimagdalena.lms.api.dto.InstructorDtos.*;
import edu.unimagdalena.lms.entities.Instructor;
import edu.unimagdalena.lms.entities.InstructorProfile;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class InstructorMapperTest {
    private final InstructorMapper mapper = Mappers.getMapper(InstructorMapper.class);

    @Test
    void toEntity_shouldMapCreate() {
        Instructor instructor = mapper.toEntity(new InstructorRequestCreate("test@uni.edu", "Test User"));

        assertThat(instructor.getId()).isNull();
        assertThat(instructor.getEmail()).isEqualTo("test@uni.edu");
        assertThat(instructor.getFullName()).isEqualTo("Test User");
        assertThat(instructor.getInstructorProfile()).isNull();
        assertThat(instructor.getCreatedAt()).isNull();
        assertThat(instructor.getUpdatedAt()).isNull();
    }

    @Test
    void toResponse_shouldMapEntity() {
        Instant createdAt = Instant.parse("2026-03-01T10:15:30Z");
        Instant updatedAt = Instant.parse("2026-03-02T10:15:30Z");

        Instructor instructor = Instructor.builder()
                .id(7L)
                .email("i@uni.edu")
                .fullName("Instructor Name")
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        InstructorResponse dto = mapper.toResponse(instructor);

        assertThat(dto.id()).isEqualTo(7L);
        assertThat(dto.email()).isEqualTo("i@uni.edu");
        assertThat(dto.fullName()).isEqualTo("Instructor Name");
    }

    @Test
    void patch_shouldIgnoreNullsAndNotTouchInstructorProfile() {
        InstructorProfile profile = InstructorProfile.builder().id(99L).build();
        Instructor instructor = Instructor.builder()
                .id(1L)
                .email("old@uni.edu")
                .fullName("Old Name")
                .instructorProfile(profile)
                .build();

        mapper.patch(instructor, new InstructorRequestUpdate(null, "New Name"));

        assertThat(instructor.getEmail()).isEqualTo("old@uni.edu");
        assertThat(instructor.getFullName()).isEqualTo("New Name");
        assertThat(instructor.getInstructorProfile()).isSameAs(profile);
        assertThat(instructor.getInstructorProfile().getId()).isEqualTo(99L);
    }
}

