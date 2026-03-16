package edu.unimagdalena.lms.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "Students")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToMany(mappedBy = "student")
    private Set<Assessment> assessments;

    @OneToMany(mappedBy = "student")
    private Set<Enrollment> enrollments;
}
