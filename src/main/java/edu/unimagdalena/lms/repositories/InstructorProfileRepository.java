package edu.unimagdalena.lms.repositories;

import edu.unimagdalena.lms.entities.InstructorProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstructorProfileRepository extends JpaRepository<InstructorProfile, Long> {

    Optional<InstructorProfile> findByInstructorId(Long instructorId);

    Optional<InstructorProfile> findByPhone(String phone);

    boolean existsByInstructorId(Long instructorId);
}
