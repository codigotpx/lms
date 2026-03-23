package edu.unimagdalena.lms.repositories;

import edu.unimagdalena.lms.entities.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByCourseId(Long courseId);
    List<Enrollment> findByStudentId(Long studentId);
    List<Enrollment> findByStatus(String status);
    long countByCourseId(Long courseId);
    boolean existsByStudentIdAndCourseIdAndStatus(Long studentId, Long courseId, String status);

    List<Enrollment> findByCourseIdOrderByEnrolledAtDesc(Long courseId);

    long countByStatus(String status);

    List<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);
}