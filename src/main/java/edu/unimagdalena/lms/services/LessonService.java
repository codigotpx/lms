package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.api.dto.LessonDtos.*;

import java.util.List;

public interface LessonService {
    LessonResponse create(LessonCreate dto);
    LessonResponse getById(Long id);
    List<LessonResponse> findAll();
    LessonResponse update(Long id, LessonUpdate dto);
    void delete(Long id);

    List<LessonResponse> findByCourseIdOrderByOrderIndexAsc(Long courseId);
    List<LessonResponse> findByCourseIdAndTitleContainingIgnoreCase(Long courseId, String title);
    long countByCourseId(Long courseId);
}

