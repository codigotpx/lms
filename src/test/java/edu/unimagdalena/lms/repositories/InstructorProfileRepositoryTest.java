package edu.unimagdalena.lms.repositories;

import edu.unimagdalena.lms.entities.Instructor;
import edu.unimagdalena.lms.entities.InstructorProfile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class InstructorProfileRepositoryTest {
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
    private InstructorProfileRepository instructorProfileRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Test
    void containerShouldStart() {
        System.out.println("Container running: " + postgres.isRunning());
    }

    @Test
    void shouldFindByInstructorId() {
        Instructor instructor = new Instructor();
        instructor.setFullName("John Doe");
        Instructor instructor2 = new Instructor();
        instructor2.setFullName("Jane Doe2");

        instructorRepository.save(instructor);
        instructorRepository.save(instructor2);

        InstructorProfile instructorProfile = new InstructorProfile();
        instructorProfile.setPhone("123456789");
        instructorProfile.setInstructor(instructor);
        InstructorProfile instructorProfile2 = new InstructorProfile();
        instructorProfile2.setPhone("123456789");
        instructorProfile2.setInstructor(instructor2);

        instructorProfileRepository.save(instructorProfile);
        instructorProfileRepository.save(instructorProfile2);

        Optional<InstructorProfile> found = instructorProfileRepository.findByInstructorId(instructorProfile.getId());

        assertTrue(found.isPresent());
    }

    @Test
    void shouldFindByPhone() {
        Instructor instructor = new Instructor();
        instructor.setFullName("John Doe");
        instructor.setEmail("john@u.co");

        Instructor instructor2 = new Instructor();
        instructor2.setFullName("Jane Doe");
        instructor2.setEmail("jane@u.co");

        instructorRepository.save(instructor);
        instructorRepository.save(instructor2);

        InstructorProfile profile1 = new InstructorProfile();
        profile1.setPhone("123456789");
        profile1.setInstructor(instructor);
        instructorProfileRepository.save(profile1);

        InstructorProfile profile2 = new InstructorProfile();
        profile2.setPhone("123456723");
        profile2.setInstructor(instructor2);
        instructorProfileRepository.save(profile2);

        Optional<InstructorProfile> found = instructorProfileRepository.findByPhone("123456789");
        Optional<InstructorProfile> found2 = instructorProfileRepository.findByPhone("000000000");

        assertTrue(found.isPresent(), "Debería encontrar el perfil con el teléfono 123456789");
        assertEquals("John Doe", found.get().getInstructor().getFullName());
        assertFalse(found2.isPresent(), "No debería encontrar un perfil con un teléfono inexistente");
    }

    @Test
    void shouldExistsByInstructorId() {
        Instructor instructor = new Instructor();
        instructor.setFullName("John Doe");
        instructor.setEmail("example@u.co");

        instructorRepository.save(instructor);

        InstructorProfile instructorProfile = new InstructorProfile();
        instructorProfile.setPhone("123456789");
        instructorProfile.setInstructor(instructor);

        instructorProfileRepository.save(instructorProfile);

        boolean exists = instructorProfileRepository.existsByInstructorId(instructorProfile.getId());
        boolean noExists = instructorProfileRepository.existsByInstructorId(99l);

        assertTrue(exists);
        assertFalse(noExists);
    }
}
