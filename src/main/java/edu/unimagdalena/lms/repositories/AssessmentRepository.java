package edu.unimagdalena.lms.repositories;

import edu.unimagdalena.lms.entities.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    List<Assessment> findByType(String type);
    List<Assessment> findByScoreGreaterThanEqual(int score);
    List<Assessment> findAssessmentByStudentId(Long studentId);
    List<Assessment> findAssessmentByCourseId(long courseId);
    List<Assessment> findAssessmentByStudentIdAndCourseId(long studentId, long courseId);

    @Query("SELECT AVG(a.score) FROM Assessment a WHERE a.course.id = :courseId")
    Double findAverageScoreByCourseId(@Param("courseId") Long courseId);
    List<Assessment> findTop10ByCourseIdOrderByScoreDesc(Long courseId);
    long countByCourseIdAndScoreGreaterThanEqual(long courseId, int minScore);
    List<Assessment> findByStudent_Email(String email);


}
