package edu.unimagdalena.lms.repositories;


import edu.unimagdalena.lms.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long > {

    Course findById(long id);
    Course findByTitle(String title);
    List<Course> findByStatus(String status);

}
