package edu.unimagdalena.lms.api.dto;

import java.time.Instant;

public class CourseDtos {
    public record CourseRequestCreate(
            Long instructorId,
            String title,
            String status,
            Boolean active
    ) {}
    public record CourseRequestUpdate(
            String title,
            String status,
            Boolean active
    ) {}

    public record CourseResponse(
            Long id,
            Long instructorId,
            String title,
            String status,
            Boolean active,
            Instant createdAt,
            Instant updatedAt
    ) {}
}
