package com.unasp.gym_app.infrastructure.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unasp.gym_app.infrastructure.entities.WorkoutExercise;

public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise, UUID> {
}
