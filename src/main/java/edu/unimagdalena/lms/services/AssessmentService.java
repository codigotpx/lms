package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.api.dto.AssessmentDtos.*;

import java.util.List;

public interface AssessmentService {
    AssessmentResponse create(AssessmentRequestCreate dto);
    AssessmentResponse getById(Long id);
    List<AssessmentResponse> findAll();
    AssessmentResponse update(Long id, AssessmentRequestUpdate dto);
    void delete(Long id);

    List<AssessmentResponse> findByType(String type);
    List<AssessmentResponse> findByScoreGreaterThanEqual(int score);
    List<AssessmentResponse> findByStudentId(Long studentId);
    List<AssessmentResponse> findByCourseId(long courseId);
    List<AssessmentResponse> findByStudentIdAndCourseId(long studentId, long courseId);
    Double findAverageScoreByCourseId(Long courseId);
    List<AssessmentResponse> findTop10ByCourseIdOrderByScoreDesc(Long courseId);
    long countByCourseIdAndScoreGreaterThanEqual(long courseId, int minScore);
    List<AssessmentResponse> findByStudentEmail(String email);
}

