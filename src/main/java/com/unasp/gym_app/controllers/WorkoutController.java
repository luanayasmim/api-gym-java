package com.unasp.gym_app.controllers;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unasp.gym_app.infrastructure.entities.Workout;
import com.unasp.gym_app.services.WorkoutService;
import com.unasp.gym_app.shared.dtos.WorkoutRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/workouts")
public class WorkoutController {

    private final WorkoutService service;

    public WorkoutController(WorkoutService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Workout> get(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getWorkout(id));
    }

    @GetMapping("/list")
    public ResponseEntity<List<Workout>> list() {
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping
    public ResponseEntity<Workout> create(@Valid @RequestBody WorkoutRequest request) {
        Workout workout = service.create(request);
        URI location = URI.create("/workouts/" + workout.getId());
        return ResponseEntity.created(location).body(workout);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable UUID id,
            @Valid @RequestBody WorkoutRequest request
    ) {
        service.update(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
