package edu.unimagdalena.lms.api.dto;

public class LessonDtos {
    public record LessonCreate(
        Long courseId,
        String title,
        int orderIndex
    ){}

    public record LessonUpdate(
            String title,
            int orderIndex
    ){}

    public record LessonResponse(
            Long id,
            Long courseId,
            String title,
            int orderIndex
    ){}
}
