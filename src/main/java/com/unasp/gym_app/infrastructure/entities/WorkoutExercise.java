package com.unasp.gym_app.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "workout_exercises")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "workout_id")
    private Workout workout;

    @Column(nullable = false)
    private String exerciseName;

    private Integer sets;
    private Integer reps;
    private Double weight;

    private String notes;
}
