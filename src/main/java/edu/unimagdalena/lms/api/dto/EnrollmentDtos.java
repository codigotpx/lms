package edu.unimagdalena.lms.api.dto;

import java.time.Instant;

public class EnrollmentDtos {
    public record EnrollmentCreate(
            Long studentId,
            Long courseId,
            String status
    ) {}

    public record EnrollmentUpdate(
            String status
    ) {}

    public record EnrollmentResponse(
            Long id,
            Long studentId,
            Long courseId,
            String status,
            Instant enrolledAt
    ) {}
}
