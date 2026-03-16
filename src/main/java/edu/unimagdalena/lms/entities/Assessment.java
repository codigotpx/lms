package edu.unimagdalena.lms.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "Assessments")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter

public class Assessment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    private String type;

    private int score;

    @Column(name = "taken_at")
    private Instant takenAt;

}
