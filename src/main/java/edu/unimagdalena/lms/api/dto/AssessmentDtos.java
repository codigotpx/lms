package edu.unimagdalena.lms.api.dto;

import java.time.Instant;

public class AssessmentDtos {
    public record AssessmentRequestCreate(
            Long studentId,
            Long courseId,
            String type,
            int score,
            Instant takenAt
    ) {}

    public record AssessmentRequestUpdate(
            String type,
            int score,
            Instant takenAt
    ) {}

    public record AssessmentResponse(
            Long id,
            Long studentId,
            Long courseId,
            String type,
            int score,
            Instant takenAt
    ) {}
}
