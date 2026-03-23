package edu.unimagdalena.lms.repositories;

import edu.unimagdalena.lms.entities.Instructor;
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
public class InstructorRepositoryTest {
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
    private InstructorRepository instructorRepository;

    @Test
    void containerShouldStart() {
        System.out.println("Container running: " + postgres.isRunning());
    }

    @Test
    void shouldFindByEmail() {
        Instructor instructor = new Instructor();
        instructor.setEmail("example@u.co");
        instructor.setFullName("Rayo");
        instructorRepository.save(instructor);

        Optional<Instructor> result = instructorRepository.findByEmail(instructor.getEmail());
        Optional<Instructor> noResult = instructorRepository.findByEmail("example2@u.co");

        assertTrue(result.isPresent());
        assertFalse(noResult.isPresent());
    }

    @Test
    void shouldFindByFullNameContainingIgnoreCase() {
        Instructor instructor = new Instructor();
        instructor.setFullName("Camilo Andres");
        instructorRepository.save(instructor);

        List<Instructor> found =  instructorRepository.findByFullNameContainingIgnoreCase("camilo andres");

        assertEquals(1, found.size());

    }

    @Test
    void shouldExistsByEmail() {
        Instructor instructor = new Instructor();
        instructor.setEmail("example@u.co");
        instructorRepository.save(instructor);

        boolean found = instructorRepository.existsByEmail(instructor.getEmail());
        boolean noFound = instructorRepository.existsByEmail("example2@u.co");

        assertTrue(found);
        assertFalse(noFound);
    }

    @Test
    void shouldFindAllByOrderByFullNameAsc() {
        Instructor i1 = new Instructor();
        i1.setFullName("Carlos Ruiz");
        i1.setEmail("carlos@u.edu.co");

        Instructor i2 = new Instructor();
        i2.setFullName("Ana Gabriel");
        i2.setEmail("ana@u.edu.co");

        Instructor i3 = new Instructor();
        i3.setFullName("Beatriz Pinzón");
        i3.setEmail("beatriz@u.edu.co");

        instructorRepository.saveAll(List.of(i1, i2, i3));

        List<Instructor> instructors = instructorRepository.findAllByOrderByFullNameAsc();

        assertNotNull(instructors);
        assertEquals(3, instructors.size());

        assertEquals("Ana Gabriel", instructors.get(0).getFullName());

        assertEquals("Beatriz Pinzón", instructors.get(1).getFullName());

        assertEquals("Carlos Ruiz", instructors.get(2).getFullName());
    }
}
