package com.unasp.gym_app.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.unasp.gym_app.infrastructure.entities.Member;
import com.unasp.gym_app.infrastructure.entities.Workout;
import com.unasp.gym_app.infrastructure.entities.WorkoutExercise;
import com.unasp.gym_app.infrastructure.repositories.IMemberRepository;
import com.unasp.gym_app.infrastructure.repositories.WorkoutRepository;
import com.unasp.gym_app.shared.dtos.WorkoutRequest;
import com.unasp.gym_app.shared.exceptions.MemberNotFoundException;
import com.unasp.gym_app.shared.exceptions.WorkoutNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final IMemberRepository memberRepository;

    public WorkoutService(WorkoutRepository workoutRepository, IMemberRepository memberRepository) {
        this.workoutRepository = workoutRepository;
        this.memberRepository = memberRepository;
    }

    public Workout getWorkout(UUID id) {
        return workoutRepository.findById(id)
                .orElseThrow(WorkoutNotFoundException::new);
    }

    public java.util.List<Workout> getAll() {
        return workoutRepository.findAll();
    }

    @Transactional
    public Workout create(WorkoutRequest request) {

        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        Workout workout = Workout.builder()
                .name(request.getName())
                .description(request.getDescription())
                .member(member)
                .createdAt(LocalDateTime.now())
                .exercises(new ArrayList<>())
                .build();

        if (request.getExercises() != null) {
            request.getExercises().forEach(exReq -> {
                WorkoutExercise ex = WorkoutExercise.builder()
                        .exerciseName(exReq.getExerciseName())
                        .sets(exReq.getSets())
                        .reps(exReq.getReps())
                        .weight(exReq.getWeight())
                        .notes(exReq.getNotes())
                        .workout(workout)
                        .build();

                workout.getExercises().add(ex);
            });
        }

        return workoutRepository.save(workout);
    }

    @Transactional
    public void update(UUID id, WorkoutRequest request) {

        Workout workout = workoutRepository.findById(id)
                .orElseThrow(WorkoutNotFoundException::new);

        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        workout.setName(request.getName());
        workout.setDescription(request.getDescription());
        workout.setMember(member);
        workout.setCreatedAt(workout.getCreatedAt());
        workout.setExercises(new ArrayList<>());

        if (request.getExercises() != null) {
            request.getExercises().forEach(exReq -> {
                WorkoutExercise ex = WorkoutExercise.builder()
                        .exerciseName(exReq.getExerciseName())
                        .sets(exReq.getSets())
                        .reps(exReq.getReps())
                        .weight(exReq.getWeight())
                        .notes(exReq.getNotes())
                        .workout(workout)
                        .build();

                workout.getExercises().add(ex);
            });
        }

        workoutRepository.save(workout);
    }

    @Transactional
    public void delete(UUID id) {
        Workout workout = workoutRepository.findById(id)
                .orElseThrow(WorkoutNotFoundException::new);
        workoutRepository.delete(workout);
    }
}
