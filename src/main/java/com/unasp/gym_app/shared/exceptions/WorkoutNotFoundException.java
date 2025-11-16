package com.unasp.gym_app.shared.exceptions;

public class WorkoutNotFoundException extends RuntimeException {
    public WorkoutNotFoundException() {
        super("Workout not found");
    }
}
