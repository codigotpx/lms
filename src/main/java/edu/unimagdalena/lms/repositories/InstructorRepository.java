package edu.unimagdalena.lms.repositories;

import edu.unimagdalena.lms.entities.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {

    Optional<Instructor> findByEmail(String email);

    List<Instructor> findByFullNameContainingIgnoreCase(String fullName);

    boolean existsByEmail(String email);

    List<Instructor> findAllByOrderByFullNameAsc();
}