package edu.unimagdalena.lms.repositories;

import edu.unimagdalena.lms.entities.Assessment;
import edu.unimagdalena.lms.entities.Course;
import edu.unimagdalena.lms.entities.Instructor;
import edu.unimagdalena.lms.entities.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CourseRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15");
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private AssessmentRepository assessmentRepository;

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
    private CourseRepository courseRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Test
    void shouldSaveCourse() {
        Course course = new Course();
        course.setTitle("Web");

        Course saved = courseRepository.save(course);

        assertNotNull(saved.getId());
    }

    @Test
    void shouldFindCourseById() {
        Course course = new Course();
        course.setTitle("Web");

        Course saved = courseRepository.save(course);

        Course found = courseRepository.findById(saved.getId()).orElse(null);

        System.out.println(saved.getId());

        assertNotNull(found);
        assertEquals("Web", found.getTitle());
    }

    @Test
    void shouldUpdateCourse() {
        Course course = new Course();
        course.setTitle("Web");
        Course saved = courseRepository.save(course);

        saved.setTitle("POO");

        Course updated = courseRepository.save(saved);

        assertEquals("POO", updated.getTitle());
    }

    @Test
    void shouldDeleteCourse() {
        Course course = new Course();
        course.setTitle("Web");
        Course saved = courseRepository.save(course);

        courseRepository.deleteById(saved.getId());

        Boolean exists = courseRepository.existsById(saved.getId());

        assertFalse(exists);
    }

    @Test
    void shouldFindAllCourse() {
        Course c1 = new Course();
        c1.setTitle("Web");

        Course c2 = new Course();
        c2.setTitle("POO");

        Course c3 = new Course();
        c3.setTitle("Bases de datos");

        courseRepository.save(c1);
        courseRepository.save(c2);
        courseRepository.save(c3);

        var courses = courseRepository.findAll();

        assertTrue(courses.size() >= 3);
    }


    @Test
    void shouldFindByTitle() {
        Course c1 = new Course();
        c1.setTitle("Web");

        Course c2 = new Course();
        c2.setTitle("POO");

        courseRepository.save(c1);
        courseRepository.save(c2);

        Optional<Course> courseOpt = courseRepository.findByTitle("Web");

        assertTrue(courseOpt.isPresent());

        Course course = courseOpt.get();

        assertEquals("Web", course.getTitle());
    }

    @Test
    void shouldFindByStatus() {
        Course c1 = new Course();
        c1.setTitle("Web");
        c1.setStatus("active");

        Course c2 = new Course();
        c2.setTitle("POO");
        c2.setStatus("active");

        Course c3 = new Course();
        c3.setTitle("Bases de datos");
        c3.setStatus("disabled");

        courseRepository.save(c1);
        courseRepository.save(c2);
        courseRepository.save(c3);

        List<Course> found = courseRepository.findByStatus("active");

        assertEquals(2, found.size());
    }

    @Test
    void shouldAllByActiveTrue() {
        Course c1 = new Course();
        c1.setTitle("Web");
        c1.setStatus("active");
        c1.setActive(true);
        Course c2 = new Course();
        c2.setTitle("Poo");
        c2.setStatus("active");
        c2.setActive(true);
        Course c3 = new Course();
        c3.setTitle("Bases de datos");
        c3.setStatus("disabled");
        c3.setActive(false);

        courseRepository.save(c1);
        courseRepository.save(c2);
        courseRepository.save(c3);

        List<Course> coursesActive = courseRepository.findByActiveTrue();

        assertEquals(2, coursesActive.size());
    }


    @Test
    void shouldFindByInstructorId() {
        Instructor instructor = new Instructor();
        instructor.setFullName("Dora la Exploradora");
        instructor.setEmail("dora@unimagdalena.edu.co");
        instructor = instructorRepository.save(instructor);

        Course c1 = new Course();
        c1.setTitle("Exploración I");
        c1.setInstructor(instructor);

        Course c2 = new Course();
        c2.setTitle("Cartografía Avanzada");
        c2.setInstructor(instructor);

        courseRepository.save(c1);
        courseRepository.save(c2);

        List<Course> found = courseRepository.findByInstructorId(instructor.getId());

        assertNotNull(found);
        assertEquals(2, found.size());
        assertTrue(found.stream().allMatch(c -> c.getTitle().contains("Exploración") || c.getTitle().contains("Cartografía")));
    }

    @Test
    void shouldCountByInstructorId() {
        Instructor instructor1 = new Instructor();
        instructor1.setEmail("usuario.example.com");
        instructor1.setFullName("Jorge el curioso");
        Instructor instructor2 = new Instructor();
        instructor2.setEmail("usuario.example.com");
        instructor2.setFullName("Rayo");

        instructor1 = instructorRepository.save(instructor1);
        instructor2 = instructorRepository.save(instructor2);

        Course course = new Course();
        course.setTitle("Web");
        course.setInstructor(instructor1);
        Course course2 = new Course();
        course2.setTitle("Poo");
        course2.setInstructor(instructor1);
        Course course3 = new Course();
        course3.setTitle("Bases de datos");
        course3.setInstructor(instructor2);

        courseRepository.save(course);
        courseRepository.save(course2);
        courseRepository.save(course3);

        assertEquals(2, courseRepository.countByInstructorId(instructor1.getId()));
    }


    @Test
    void shouldFindByTitleContainingIgnoreCaseAndActiveTrue() {
        Course course1 = new Course();
        course1.setTitle("Web");
        course1.setStatus("active");
        course1.setActive(true);
        Course course2 = new Course();
        course2.setTitle("Web");
        course2.setStatus("active");
        course2.setActive(true);
        Course course3 = new Course();
        course3.setTitle("Web");
        course3.setStatus("disabled");
        course3.setActive(false);

        courseRepository.save(course1);
        courseRepository.save(course2);
        courseRepository.save(course3);

        List<Course> found = courseRepository.findByTitleContainingIgnoreCaseAndActiveTrue("Web");
        assertEquals(2, found.size());
    }

    @Test
    void shouldFindByStudent_Email() {
        Student student = new Student();
        student.setEmail("dora.example.com");
        student.setFullName("Jorge el curioso");

        Student student2 = new Student();
        student2.setEmail("usuario.example.com");
        student2.setFullName("Rayo");

        studentRepository.save(student);
        studentRepository.save(student2);

        Assessment assessment = new Assessment();
        assessment.setStudent(student);
        assessment.setStudent(student2);
        Assessment assessment2 = new Assessment();
        assessment2.setStudent(student2);

        assessmentRepository.save(assessment);
        assessmentRepository.save(assessment2);

        List<Assessment> found = assessmentRepository.findByStudent_Email("dora.example.com");

        assertEquals(2, found.size());

    }

}