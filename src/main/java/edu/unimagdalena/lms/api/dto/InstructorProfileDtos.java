package edu.unimagdalena.lms.api.dto;

import java.time.Instant;

public class InstructorProfileDtos {
    public record InstructorProfileRequestCreate(
            Long instructorId,
            String phone,
            String bio
    ) {}

    public record InstructorProfileRequestUpdate(
            String phone,
            String bio
    ) {}

    public record InstructorProfileResponse(
            Long id,
            Long instructorId,
            String phone,
            String bio
    ) {}
}
