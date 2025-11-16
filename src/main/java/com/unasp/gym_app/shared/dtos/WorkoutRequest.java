package com.unasp.gym_app.shared.dtos;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutRequest {

    @NotBlank(message = "Workout name is required")
    @Length(min = 2, max = 100, message = "Workout name must be between 2 and 100 characters")
    private String name;

    private String description;

    @NotNull(message = "Member ID is required")
    private java.util.UUID memberId;

    @Valid
    private List<WorkoutExerciseRequest> exercises;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WorkoutExerciseRequest {

        @NotBlank(message = "Exercise name is required")
        private String exerciseName;

        private Integer sets;
        private Integer reps;
        private Double weight;

        private String notes;
    }
}
