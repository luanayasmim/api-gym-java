package com.unasp.gym_app.infrastructure.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unasp.gym_app.infrastructure.entities.Member;

public interface IMemberRepository extends JpaRepository<Member, UUID> {

    // public Member findByEmail(String email);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByEmailAndIsActiveTrue(String email);
    boolean existsByEmail(String email);
}
