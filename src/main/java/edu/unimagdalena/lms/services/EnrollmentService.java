package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.api.dto.EnrollmentDtos.*;

import java.util.List;

public interface EnrollmentService {
    EnrollmentResponse create(EnrollmentCreate dto);
    EnrollmentResponse getById(Long id);
    List<EnrollmentResponse> findAll();
    EnrollmentResponse update(Long id, EnrollmentUpdate dto);
    void delete(Long id);

    List<EnrollmentResponse> findByCourseId(Long courseId);
    List<EnrollmentResponse> findByStudentId(Long studentId);
    List<EnrollmentResponse> findByStatus(String status);
    long countByCourseId(Long courseId);
    boolean existsByStudentIdAndCourseIdAndStatus(Long studentId, Long courseId, String status);
    List<EnrollmentResponse> findByCourseIdOrderByEnrolledAtDesc(Long courseId);
    long countByStatus(String status);
    List<EnrollmentResponse> findByStudentIdAndCourseId(Long studentId, Long courseId);
}

