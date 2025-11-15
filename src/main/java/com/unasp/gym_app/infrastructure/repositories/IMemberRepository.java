package com.unasp.gym_app.infrastructure.repositories;

import com.unasp.gym_app.infrastructure.entities.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IMemberRepository extends JpaRepository<Member, UUID> {

    public Member findByEmail(String email);
}
