package edu.unimagdalena.lms.services.mapper;

import edu.unimagdalena.lms.api.dto.StudentDtos.*;
import edu.unimagdalena.lms.entities.Student;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class StudentMapperTest {
    private final StudentMapper mapper = Mappers.getMapper(StudentMapper.class);

    @Test
    void toEntity_shouldMapCreate() {
        Student student = mapper.toEntity(new StudentCreate("camilo@.com", "camilo"));
        assertThat(student.getFullName()).isEqualTo("camilo");
    }

    @Test
    void toResponse_shouldMapCreate() {
        var a = Student.builder().id(5L).fullName("Camilo").email("camilo@com").build();
        StudentResponse dto = mapper.toResponse(a);
        assertThat(dto.id()).isEqualTo(5L);
    }
}
