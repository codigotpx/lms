package edu.unimagdalena.lms.repositories;

import edu.unimagdalena.lms.entities.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {

    Instructor findById(long id);
    Instructor findByEmail(String email);
    Instructor findByFullName(String name);
}
