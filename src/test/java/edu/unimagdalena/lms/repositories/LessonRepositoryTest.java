package edu.unimagdalena.lms.repositories;

import edu.unimagdalena.lms.entities.Course;
import edu.unimagdalena.lms.entities.Lesson;
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
public class LessonRepositoryTest {
    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {

        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

    }

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    LessonRepository lessonRepository;

    @Test
    void containerShouldStart() {
        System.out.println("Container running: " + postgres.isRunning());
    }

    @Test
    void shouldFindByCourseIdOrderByOrderIndexAsc() {
        Course course = new Course();
        course.setTitle("Programación React");
        courseRepository.save(course);

        Lesson l3 = new Lesson();
        l3.setTitle("Hooks Avanzados");
        l3.setOrderIndex(3);
        l3.setCourse(course);

        Lesson l1 = new Lesson();
        l1.setTitle("Introducción a Componentes");
        l1.setOrderIndex(1);
        l1.setCourse(course);

        Lesson l2 = new Lesson();
        l2.setTitle("Manejo de Estado");
        l2.setOrderIndex(2);
        l2.setCourse(course);

        lessonRepository.saveAll(List.of(l3, l1, l2));

        List<Lesson> lessons = lessonRepository.findByCourseIdOrderByOrderIndexAsc(course.getId());

        assertNotNull(lessons);
        assertEquals(3, lessons.size());

        assertEquals(1, lessons.get(0).getOrderIndex(), "La primera lección debe ser la de índice 1");
        assertEquals("Introducción a Componentes", lessons.get(0).getTitle());

        assertEquals(2, lessons.get(1).getOrderIndex(), "La segunda lección debe ser la de índice 2");

        assertEquals(3, lessons.get(2).getOrderIndex(), "La tercera lección debe ser la de índice 3");
    }

    @Test
    void shouldFindByCourseIdAndTitleContainingIgnoreCase() {
        Course course = new Course();
        course.setTitle("Master en Java");
        courseRepository.save(course);

        Lesson l1 = new Lesson();
        l1.setTitle("Introducción a Spring Boot");
        l1.setCourse(course);

        Lesson l2 = new Lesson();
        l2.setTitle("Testing con SPRING Security");
        l2.setCourse(course);

        Lesson l3 = new Lesson();
        l3.setTitle("Manejo de Bases de Datos");
        l3.setCourse(course);

        lessonRepository.saveAll(List.of(l1, l2, l3));

        List<Lesson> results = lessonRepository.findByCourseIdAndTitleContainingIgnoreCase(course.getId(), "spring");

        assertNotNull(results);
        assertEquals(2, results.size());

        assertTrue(results.stream().anyMatch(l -> l.getTitle().contains("Spring Boot")));
        assertTrue(results.stream().anyMatch(l -> l.getTitle().contains("SPRING Security")));

        assertFalse(results.stream().anyMatch(l -> l.getTitle().contains("Bases de Datos")));
    }

    @Test
    void shouldCountByCourseId() {
        Course course = new Course();
        course.setTitle("Master en Java");
        courseRepository.save(course);
        Course course2 = new Course();
        course2.setTitle("Master en Java");
        courseRepository.save(course2);

        Lesson l1 = new Lesson();
        l1.setCourse(course);
        Lesson l2 = new Lesson();
        l2.setCourse(course2);
        Lesson l3 = new Lesson();
        l3.setCourse(course2);
        lessonRepository.saveAll(List.of(l1, l2, l3));

        long results = lessonRepository.countByCourseId(course2.getId());

        assertEquals(2, results);
    }


}
