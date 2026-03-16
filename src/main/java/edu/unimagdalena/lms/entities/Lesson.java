package edu.unimagdalena.lms.entities;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Lessons")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter

public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    private String title;

    @Column(name = "order_index")
    private int orderIndex;
}
