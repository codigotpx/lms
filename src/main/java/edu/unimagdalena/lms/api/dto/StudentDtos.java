package edu.unimagdalena.lms.api.dto;

import java.time.Instant;

public class StudentDtos {
    public record StudentCreate(
        String email,
        String fullName
    ) {}

    public record StudentUpdate(
            String email,
            String fullName
    ){}

    public record StudentResponse(
            Long id,
            String email,
            String fullName
    ){}

    public record StudentRequest(
            Long id,
            String email,
            String fullName
    ){}

    public record StudentUpdateRequest(
            String email,
            String fullName
    ) {}
}
