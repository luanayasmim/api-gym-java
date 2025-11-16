package com.unasp.gym_app.infrastructure.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unasp.gym_app.infrastructure.entities.Workout;

public interface WorkoutRepository extends JpaRepository<Workout, UUID> {
}
