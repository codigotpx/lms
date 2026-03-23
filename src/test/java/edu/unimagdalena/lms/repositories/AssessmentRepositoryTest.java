package edu.unimagdalena.lms.repositories;


import edu.unimagdalena.lms.entities.Assessment;
import edu.unimagdalena.lms.entities.Course;
import edu.unimagdalena.lms.entities.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.AutoConfigureDataJpa;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static edu.unimagdalena.lms.repositories.CourseRepositoryTest.postgres;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AssessmentRepositoryTest {
    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {

        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

    }

    @Test
    void containerShouldStart() {
        System.out.println("Container running: " + postgres.isRunning());
    }

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;


    @Test
    void findByType() {
        Assessment assessment = new Assessment();
        assessment.setType("Final exam");
        Assessment assessment2 = new Assessment();
        assessment2.setType("Final exam");
        Assessment assessment3 = new Assessment();
        assessment3.setType("Middle exam");

        assessmentRepository.save(assessment);
        assessmentRepository.save(assessment2);
        assessmentRepository.save(assessment3);

        List<Assessment> assessments = assessmentRepository.findByType("Final exam");

        assertEquals(2, assessments.size());
    }

    @Test
    void shouldFindByScoreGreaterThan() {
        Assessment assessment1 = new Assessment();
        assessment1.setScore(2000);
        Assessment assessment2 = new Assessment();
        assessment2.setScore(3000);
        Assessment assessment3 = new Assessment();
        assessment3.setScore(1000);

        assessmentRepository.save(assessment1);
        assessmentRepository.save(assessment2);
        assessmentRepository.save(assessment3);

        List<Assessment> assessments = assessmentRepository.findByScoreGreaterThanEqual(2000);

        assertEquals(2, assessments.size());
    }

    @Test
    void shouldFindAssessmentByStudentId() {
        Student student = new Student();
        student.setFullName("John Doe");
        student.setEmail("example.com.co");
        Student student2 = new Student();
        student2.setFullName("John 2");
        student2.setEmail("example.com.co");
        Student student3 = new Student();
        student3.setFullName("John 3");
        student3.setEmail("example.com.co");

        studentRepository.save(student);
        studentRepository.save(student2);
        studentRepository.save(student3);

        Assessment assessment1 = new Assessment();
        assessment1.setStudent(student);
        Assessment assessment2 = new Assessment();
        assessment2.setStudent(student2);
        Assessment assessment3 = new Assessment();
        assessment3.setStudent(student3);
        Assessment assessment4 = new Assessment();
        assessment4.setStudent(student);

        assessmentRepository.save(assessment1);
        assessmentRepository.save(assessment2);
        assessmentRepository.save(assessment3);
        assessmentRepository.save(assessment4);

        List<Assessment> assessments = assessmentRepository.findAssessmentByStudentId(student.getId());
        assertEquals(2, assessments.size());
    }

    @Test
    void shouldFindAssessmentByCourseId() {
        Course course = new Course();
        course.setTitle("Web");
        Course course2 = new Course();
        course2.setTitle("Web2");
        Course course3 = new Course();
        course3.setTitle("Web3");

        courseRepository.save(course);
        courseRepository.save(course2);
        courseRepository.save(course3);

        Assessment assessment1 = new Assessment();
        assessment1.setCourse(course);
        Assessment assessment2 = new Assessment();
        assessment2.setCourse(course2);
        Assessment assessment3 = new Assessment();
        assessment3.setCourse(course3);
        Assessment assessment4 = new Assessment();
        assessment4.setCourse(course3);

        assessmentRepository.save(assessment1);
        assessmentRepository.save(assessment2);
        assessmentRepository.save(assessment3);
        assessmentRepository.save(assessment4);

        List<Assessment> assessments = assessmentRepository.findAssessmentByCourseId(course3.getId());

        assertEquals(2, assessments.size());
    }

    @Test
    void shouldFindAssessmentByCourseIdAndStudentId() {
        Course course = new Course();
        course.setTitle("Web");
        Course course2 = new Course();
        course2.setTitle("Web2");

        Student student = new Student();
        student.setFullName("John Doe");
        student.setEmail("example.com.co");
        Student student2 = new Student();
        student2.setFullName("John 2");
        student2.setEmail("example.com.co");

        studentRepository.save(student);
        studentRepository.save(student2);
        courseRepository.save(course);
        courseRepository.save(course2);

        Assessment assessment1 = new Assessment();
        assessment1.setCourse(course);
        assessment1.setStudent(student);
        Assessment assessment2 = new Assessment();
        assessment2.setCourse(course2);
        assessment2.setStudent(student2);
        Assessment assessment3 = new Assessment();
        assessment3.setCourse(course);
        assessment3.setStudent(student);
        Assessment assessment4 = new Assessment();
        assessment4.setCourse(course2);
        assessment4.setStudent(student);

        assessmentRepository.save(assessment1);
        assessmentRepository.save(assessment2);
        assessmentRepository.save(assessment3);
        assessmentRepository.save(assessment4);

        List<Assessment> found = assessmentRepository.findAssessmentByStudentIdAndCourseId(student.getId(), course.getId());

        assertEquals(2, found.size());
    }

    @Test
    void shouldFindAverageScoreByCourseId() {
        Course course = new Course();
        course.setTitle("Web");
        Course course2 = new Course();
        course2.setTitle("Web2");
        Course course3 = new Course();
        course3.setTitle("Web3");

        courseRepository.save(course);
        courseRepository.save(course2);
        courseRepository.save(course3);

        Assessment assessment1 = new Assessment();
        assessment1.setCourse(course);
        assessment1.setScore(10);
        Assessment assessment2 = new Assessment();
        assessment2.setCourse(course2);
        assessment2.setScore(5);
        Assessment assessment3 = new Assessment();
        assessment3.setCourse(course3);
        assessment3.setScore(10);
        Assessment assessment4 = new Assessment();
        assessment4.setCourse(course);
        assessment4.setScore(8);
        Assessment assessment5 = new Assessment();
        assessment5.setCourse(course);
        assessment5.setScore(9);

        assessmentRepository.save(assessment1);
        assessmentRepository.save(assessment2);
        assessmentRepository.save(assessment3);
        assessmentRepository.save(assessment4);
        assessmentRepository.save(assessment5);

        Double averageCourse1 =  assessmentRepository.findAverageScoreByCourseId(course.getId());

        assertEquals(9.0, averageCourse1);
    }

    @Test
    void shouldFindTop10ByCourseIdOrderByScoreDesc() {
        Course course1 = new Course();
        course1.setTitle("Web");
        Course course2 = new Course();
        course2.setTitle("Web2");
        Course course3 = new Course();
        course3.setTitle("Web3");
        courseRepository.save(course1);
        courseRepository.save(course2);
        courseRepository.save(course3);

        Assessment assessment1 = new Assessment();
        assessment1.setCourse(course1);
        assessment1.setScore(10);
        Assessment assessment2 = new Assessment();
        assessment2.setCourse(course2);
        assessment2.setScore(5);
        Assessment assessment3 = new Assessment();
        assessment3.setCourse(course3);
        assessment3.setScore(10);
        Assessment assessment4 = new Assessment();
        assessment4.setCourse(course3);
        assessment4.setScore(8);
        Assessment assessment5 = new Assessment();
        assessment5.setCourse(course3);
        assessment5.setScore(9);

        assessmentRepository.save(assessment1);
        assessmentRepository.save(assessment2);
        assessmentRepository.save(assessment3);
        assessmentRepository.save(assessment4);
        assessmentRepository.save(assessment5);

        List<Assessment>  assessments = assessmentRepository.findTop10ByCourseIdOrderByScoreDesc(course3.getId());

        assertEquals(10, assessments.get(0).getScore());
        assertEquals(9, assessments.get(1).getScore());
        assertEquals(8, assessments.get(2).getScore());
    }

    @Test
    void shouldCountByCourseIdAndScoreGreaterThanEqual() {
        Course course = new Course();
        course.setTitle("Estructura de Datos");
        courseRepository.save(course);

        Course otherCourse = new Course();
        otherCourse.setTitle("Cálculo");
        courseRepository.save(otherCourse);

        Assessment a1 = new Assessment();
        a1.setCourse(course);
        a1.setScore(85);

        Assessment a2 = new Assessment();
        a2.setCourse(course);
        a2.setScore(60);

        Assessment a3 = new Assessment();
        a3.setCourse(course);
        a3.setScore(45);

        Assessment a4 = new Assessment();
        a4.setCourse(otherCourse);
        a4.setScore(90);

        assessmentRepository.saveAll(List.of(a1, a2, a3, a4));

        long found = assessmentRepository.countByCourseIdAndScoreGreaterThanEqual(course.getId(), 60);

        assertEquals(2, found);
    }
}
