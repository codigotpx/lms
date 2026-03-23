package edu.unimagdalena.lms.repositories;

import edu.unimagdalena.lms.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmail(String email);

    List<Student> findByFullNameContainingIgnoreCase(String fullName);

    boolean existsByEmail(String email);

    @Query("SELECT s FROM Student s JOIN Enrollment e ON s.id = e.student.id WHERE e.course.id = :courseId")
    List<Student> findByCourseId(@Param("courseId") Long courseId);
}
