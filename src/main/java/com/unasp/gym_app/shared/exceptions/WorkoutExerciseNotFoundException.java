package com.unasp.gym_app.shared.exceptions;

public class WorkoutExerciseNotFoundException extends RuntimeException {
    public WorkoutExerciseNotFoundException() {
        super("Workout exercise not found");
    }
}
