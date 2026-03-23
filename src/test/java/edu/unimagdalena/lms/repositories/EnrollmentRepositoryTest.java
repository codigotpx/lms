package edu.unimagdalena.lms.repositories;

import edu.unimagdalena.lms.entities.Course;
import edu.unimagdalena.lms.entities.Enrollment;
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

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EnrollmentRepositoryTest {
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
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void shouldFindByCourseId() {
        Course course1 = new Course();
        course1.setTitle("Course 1");
        Course course2 = new Course();
        course2.setTitle("Course 2");


        courseRepository.save(course1);
        courseRepository.save(course2);

        Enrollment enrollment1 = new Enrollment();
        enrollment1.setCourse(course1);
        Enrollment enrollment2 = new Enrollment();
        enrollment2.setCourse(course2);
        Enrollment enrollment3 = new Enrollment();
        enrollment3.setCourse(course1);

        enrollmentRepository.save(enrollment1);
        enrollmentRepository.save(enrollment2);
        enrollmentRepository.save(enrollment3);

        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(course1.getId());

        assertEquals(2, enrollments.size());
    }

    @Test
    void shouldFindByStudentId() {
        Student student1 = new Student();
        student1.setEmail("example.com.co");
        Student student2 = new Student();
        student2.setEmail("example2.com.co");

        studentRepository.save(student1);
        studentRepository.save(student2);

        Enrollment enrollment1 = new Enrollment();
        enrollment1.setStudent(student1);
        Enrollment enrollment2 = new Enrollment();
        enrollment2.setStudent(student2);
        Enrollment enrollment3 = new Enrollment();
        enrollment3.setStudent(student1);

        enrollmentRepository.save(enrollment1);
        enrollmentRepository.save(enrollment2);
        enrollmentRepository.save(enrollment3);

        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(student1.getId());

        assertEquals(2, enrollments.size());
    }

    @Test
    void shouldFindByStatus() {
        Enrollment enrollment1 = new Enrollment();
        enrollment1.setStatus("ACTIVE");
        Enrollment enrollment2 = new Enrollment();
        enrollment2.setStatus("ACTIVE");
        Enrollment enrollment3 = new Enrollment();
        enrollment3.setStatus("DISABLED");

        enrollmentRepository.save(enrollment1);
        enrollmentRepository.save(enrollment2);
        enrollmentRepository.save(enrollment3);

        List<Enrollment> enrollments = enrollmentRepository.findByStatus("ACTIVE");

        assertEquals(2, enrollments.size());
    }

    @Test
    void shouldCountByCourseId() {
        Course course1 = new Course();
        course1.setTitle("Course 1");
        Course course2 = new Course();
        course2.setTitle("Course 2");

        courseRepository.save(course1);
        courseRepository.save(course2);

        Enrollment enrollment1 = new Enrollment();
        enrollment1.setCourse(course1);
        Enrollment enrollment2 = new Enrollment();
        enrollment2.setCourse(course2);
        Enrollment enrollment3 = new Enrollment();
        enrollment3.setCourse(course1);

        enrollmentRepository.save(enrollment1);
        enrollmentRepository.save(enrollment2);
        enrollmentRepository.save(enrollment3);

        long count = enrollmentRepository.countByCourseId(course1.getId());

        assertEquals(2, count);
    }

    @Test
    void shouldVerifyIfActiveEnrollmentExists() {

        Student student = new Student();
        student.setFullName("Lina");
        student.setEmail("lina@unimagdalena.edu.co");
        studentRepository.save(student);

        Course course = new Course();
        course.setTitle("Sistemas Operativos");
        courseRepository.save(course);

        Enrollment activeEnrollment = new Enrollment();
        activeEnrollment.setStudent(student);
        activeEnrollment.setCourse(course);
        activeEnrollment.setStatus("ACTIVE");
        enrollmentRepository.save(activeEnrollment);

        boolean existsActive = enrollmentRepository.existsByStudentIdAndCourseIdAndStatus(
                student.getId(), course.getId(), "ACTIVE");

        boolean existsCancelled = enrollmentRepository.existsByStudentIdAndCourseIdAndStatus(
                student.getId(), course.getId(), "CANCELLED");

        boolean existsWrongCourse = enrollmentRepository.existsByStudentIdAndCourseIdAndStatus(
                student.getId(), 999L, "ACTIVE");

        assertTrue(existsActive, "Debería encontrar la inscripción activa");
        assertFalse(existsCancelled, "No debería encontrar una inscripción cancelada");
        assertFalse(existsWrongCourse, "No debería encontrar inscripción para un curso inexistente");
    }

    @Test
    void shouldFindByCourseIdOrderByEnrolledAtDesc() {
        Course course = new Course();
        course.setTitle("Redes de Computadores");
        courseRepository.save(course);

        Student s1 = new Student(); s1.setFullName("Juan"); s1.setEmail("juan@u.co");
        Student s2 = new Student(); s2.setFullName("Maria"); s2.setEmail("maria@u.co");
        Student s3 = new Student(); s3.setFullName("Pedro"); s3.setEmail("pedro@u.co");
        studentRepository.saveAll(List.of(s1, s2, s3));

        Enrollment e1 = new Enrollment();
        e1.setCourse(course);
        e1.setStudent(s1);
        e1.setEnrolledAt(Instant.now().minus(java.time.Duration.ofDays(2))); // Hace 2 días

        Enrollment e2 = new Enrollment();
        e2.setCourse(course);
        e2.setStudent(s2);
        e2.setEnrolledAt(Instant.now().minus(java.time.Duration.ofHours(5))); // Hace 5 horas

        Enrollment e3 = new Enrollment();
        e3.setCourse(course);
        e3.setStudent(s3);
        e3.setEnrolledAt(Instant.now());

        enrollmentRepository.saveAll(List.of(e1, e2, e3));

        List<Enrollment> enrollments = enrollmentRepository.findByCourseIdOrderByEnrolledAtDesc(course.getId());

        assertNotNull(enrollments);
        assertEquals(3, enrollments.size());

        assertEquals(s3.getFullName(), enrollments.get(0).getStudent().getFullName(), "El más reciente debe ser Pedro");

        assertEquals(s1.getFullName(), enrollments.get(2).getStudent().getFullName(), "El más antiguo debe ser Juan");
    }

    @Test
    void shouldCountByStatus() {
        Enrollment e1 = new Enrollment();
        e1.setStatus("ACTIVE");
        Enrollment e2 = new Enrollment();
        e2.setStatus("CANCELLED");
        Enrollment e3 = new Enrollment();
        e3.setStatus("COMPLETED");
        enrollmentRepository.saveAll(List.of(e1, e2, e3));

        long count = enrollmentRepository.countByStatus("COMPLETED");

        assertEquals(1, count);
    }

    @Test
    void shouldFindByCourseIdAndCourseId() {
        Course course = new Course();
        course.setTitle("Redes de Computadores");
        courseRepository.save(course);

        Student student = new Student();
        student.setFullName("Juan");
        student.setEmail("Juan@u.co");
        Student student2 = new Student();
        student2.setFullName("Maria");
        student2.setEmail("Maria@u.co");
        studentRepository.save(student);
        studentRepository.save(student2);

        Enrollment e1 = new Enrollment();
        e1.setCourse(course);
        e1.setStudent(student);
        Enrollment e2 = new Enrollment();
        e2.setCourse(course);
        e2.setStudent(student);
        Enrollment e3 = new Enrollment();
        e3.setCourse(course);
        e3.setStudent(student2);

        enrollmentRepository.saveAll(List.of(e1, e2, e3));

        List<Enrollment> found = enrollmentRepository.findByStudentIdAndCourseId(student.getId(), course.getId());

        assertEquals(2, found.size());
    }
}
