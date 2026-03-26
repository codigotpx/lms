package edu.unimagdalena.lms.api.dto;

import java.time.Instant;

public class InstructorDtos {
    public record InstructorRequestCreate(
            String email,
            String fullName
    ){}

    public record InstructorRequestUpdate(
            String email,
            String fullName
    ){}

    public record InstructorResponse(
            Long id,
            String email,
            String fullName
    ) {}
}
