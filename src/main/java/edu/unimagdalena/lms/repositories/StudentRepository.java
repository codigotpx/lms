package edu.unimagdalena.lms.repositories;

import edu.unimagdalena.lms.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findById(long id);
    Student findByEmail(String email);
    Student findByFullName(String name);
}
