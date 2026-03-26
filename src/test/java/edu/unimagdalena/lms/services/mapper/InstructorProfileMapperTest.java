package edu.unimagdalena.lms.services.mapper;

import edu.unimagdalena.lms.api.dto.InstructorProfileDtos.*;
import edu.unimagdalena.lms.entities.Instructor;
import edu.unimagdalena.lms.entities.InstructorProfile;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class InstructorProfileMapperTest {
    private final InstructorProfileMapper mapper = Mappers.getMapper(InstructorProfileMapper.class);

    @Test
    void toEntity_shouldMapCreateIgnoringInstructor() {
        InstructorProfile profile = mapper.toEntity(new InstructorProfileRequestCreate(10L, "123", "Bio"));

        assertThat(profile.getId()).isNull();
        assertThat(profile.getInstructor()).isNull();
        assertThat(profile.getPhone()).isEqualTo("123");
        assertThat(profile.getBio()).isEqualTo("Bio");
    }

    @Test
    void toResponse_shouldMapEntity() {
        InstructorProfile profile = InstructorProfile.builder()
                .id(5L)
                .instructor(Instructor.builder().id(9L).build())
                .phone("555")
                .bio("Hi")
                .build();

        InstructorProfileResponse dto = mapper.toResponse(profile);

        assertThat(dto.id()).isEqualTo(5L);
        assertThat(dto.instructorId()).isEqualTo(9L);
        assertThat(dto.phone()).isEqualTo("555");
        assertThat(dto.bio()).isEqualTo("Hi");
    }

    @Test
    void patch_shouldIgnoreNulls() {
        InstructorProfile profile = InstructorProfile.builder()
                .id(1L)
                .phone("old")
                .bio("oldbio")
                .build();

        mapper.patch(profile, new InstructorProfileRequestUpdate(null, "newbio"));

        assertThat(profile.getPhone()).isEqualTo("old");
        assertThat(profile.getBio()).isEqualTo("newbio");
    }
}

