package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.api.dto.CourseDtos.*;

import java.util.List;

public interface CourseService {
    CourseResponse create(CourseRequestCreate dto);
    CourseResponse getById(Long id);
    List<CourseResponse> findAll();
    CourseResponse update(Long id, CourseRequestUpdate dto);
    void delete(Long id);


    List<CourseResponse> findByStatus(String status);
    List<CourseResponse> findByActiveTrue();
    List<CourseResponse> findByInstructorId(Long instructorId);
    long countByInstructorId(Long instructorId);
    List<CourseResponse> findByTitleContaining(String title);


}
