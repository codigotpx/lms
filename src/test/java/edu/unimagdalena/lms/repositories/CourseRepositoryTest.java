package edu.unimagdalena.lms.repositories;

import edu.unimagdalena.lms.entities.Course;
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

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CourseRepositoryTest {

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
    private CourseRepository courseRepository;

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

        var titleFound = courseRepository.findByTitle("Web");

        assertEquals("Web", titleFound.getTitle());
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

}