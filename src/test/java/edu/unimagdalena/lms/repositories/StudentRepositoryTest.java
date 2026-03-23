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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StudentRepositoryTest {

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
    StudentRepository studentRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    EnrollmentRepository enrollmentRepository;

    @Test
    void containerShouldStart() {
        System.out.println("Container running: " + postgres.isRunning());
    }


    @Test
    void shouldFindByEmail() {
        Student student1 = new  Student();
        student1.setEmail("example@u.co");
        student1.setFullName("Jorge el curioso");
        Student student2 = new  Student();
        student2.setEmail("example2@u.co");
        student2.setFullName("Jorge el curioso2");

        studentRepository.save(student1);
        studentRepository.save(student2);

        Optional<Student> found = studentRepository.findByEmail(student1.getEmail());

        assertTrue(found.isPresent());
    }

    @Test
    void shouldFindStudentByFullNameContainingIgnoreCase() {
        Student s1 = new Student();
        s1.setFullName("Lina Maria Cerpa");
        s1.setEmail("lina.cerpa@u.edu.co");

        Student s2 = new Student();
        s2.setFullName("Carlos Mario Rodriguez");
        s2.setEmail("carlos.mario@u.edu.co");

        Student s3 = new Student();
        s3.setFullName("Mariana Velez");
        s3.setEmail("mariana.v@u.edu.co");

        studentRepository.saveAll(List.of(s1, s2, s3));

        List<Student> results = studentRepository.findByFullNameContainingIgnoreCase("MARI");

        assertNotNull(results);
        assertEquals(3, results.size(), "Debería encontrar los 3 estudiantes que contienen 'MARI'");

        assertTrue(results.stream().anyMatch(s -> s.getFullName().contains("Maria")));
        assertTrue(results.stream().anyMatch(s -> s.getFullName().contains("Mario")));
        assertTrue(results.stream().anyMatch(s -> s.getFullName().contains("Mariana")));

        List<Student> noResults = studentRepository.findByFullNameContainingIgnoreCase("Zulema");

        assertTrue(noResults.isEmpty(), "No debería encontrar resultados para un nombre inexistente");
    }

    @Test
    void shouldVerifyIfStudentExistsByEmail() {
        String emailExistente = "lina.cerpa@u.edu.co";

        Student student = new Student();
        student.setFullName("Lina Maria Cerpa");
        student.setEmail(emailExistente);

        studentRepository.save(student);

        boolean existe = studentRepository.existsByEmail(emailExistente);
        boolean noExiste = studentRepository.existsByEmail("correo.falso@u.edu.co");

        assertTrue(existe, "Debería retornar true para un email que ya está en la base de datos");
        assertFalse(noExiste, "Debería retornar false para un email que no ha sido registrado");
    }

    @Test
    void shouldFindStudentsByCourseId() {
        Course course = new Course();
        course.setTitle("Ingeniería de Software");
        courseRepository.save(course);

        Student s1 = new Student();
        s1.setFullName("Lina Maria Cerpa");
        s1.setEmail("lina@u.edu.co");

        Student s2 = new Student();
        s2.setFullName("Carlos Mario");
        s2.setEmail("carlos@u.edu.co");

        Student s3 = new Student();
        s3.setFullName("Pedro Solo");
        s3.setEmail("pedro@u.co");

        studentRepository.saveAll(List.of(s1, s2, s3));

        Enrollment e1 = new Enrollment();
        e1.setStudent(s1);
        e1.setCourse(course);
        e1.setStatus("ACTIVE");

        Enrollment e2 = new Enrollment();
        e2.setStudent(s2);
        e2.setCourse(course);
        e2.setStatus("ACTIVE");

        enrollmentRepository.saveAll(List.of(e1, e2));

        List<Student> studentsInCourse = studentRepository.findByCourseId(course.getId());

        assertNotNull(studentsInCourse);
        assertEquals(2, studentsInCourse.size(), "Debería haber 2 estudiantes inscritos");

        assertTrue(studentsInCourse.stream().anyMatch(s -> s.getFullName().equals("Lina Maria Cerpa")));
        assertTrue(studentsInCourse.stream().anyMatch(s -> s.getFullName().equals("Carlos Mario")));

        assertFalse(studentsInCourse.stream().anyMatch(s -> s.getFullName().equals("Pedro Solo")));
    }
}
