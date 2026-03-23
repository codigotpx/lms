package edu.unimagdalena.lms.repositories;


import edu.unimagdalena.lms.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long > {

    Optional<Course> findByTitle(String title);
    List<Course> findByStatus(String status);
    List<Course> findByActiveTrue();

    List<Course> findByInstructorId(Long instructorId);
    long countByInstructorId(Long instructorId);

    List<Course> findByTitleContainingIgnoreCaseAndActiveTrue(String title);
}
