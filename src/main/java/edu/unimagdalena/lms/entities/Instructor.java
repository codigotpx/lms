package edu.unimagdalena.lms.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "Instructor")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter

public class Instructor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;

    @Column(name = "full_name")
    private String fullName;
    
    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToMany(mappedBy = "instructor")
    private Set<Course> courses;

    @OneToOne(mappedBy = "instructor")
    private InstructorProfile instructorProfile;
}
