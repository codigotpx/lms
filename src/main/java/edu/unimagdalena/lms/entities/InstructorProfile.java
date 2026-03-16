package edu.unimagdalena.lms.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Instructors_Profiles")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter

public class InstructorProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "instructor_id", referencedColumnName = "id")
    private Instructor instructor;

    private String phone;

    private String bio;
}
