package com.unasp.gym_app.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unasp.gym_app.infrastructure.entities.Member;
import com.unasp.gym_app.infrastructure.repositories.IMemberRepository;
import com.unasp.gym_app.shared.dtos.MemberRequest;
import com.unasp.gym_app.shared.exceptions.DuplicateEmailException;
import com.unasp.gym_app.shared.exceptions.MemberNotFoundException;

@Service
public class MemberService {

    @Autowired
    private IMemberRepository memberRepository;

    public Member getMemberById(UUID id) {
        return memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    @Transactional
    public Member createMember(MemberRequest request) {

        var existingMember = memberRepository.findByEmail(request.getEmail());
        if (existingMember != null) {
            throw new DuplicateEmailException(request.getEmail());
        }

        var member = Member.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .birthDate(request.getBirthDate())
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .build();

        return memberRepository.saveAndFlush(member);
    }

    @Transactional
    public void updateMember(UUID id, MemberRequest request) {

        var existingByEmail = memberRepository.findByEmail(request.getEmail());
        
        // Se existir um membro com o mesmo email e NÃO for o mesmo ID → conflito
        existingByEmail.ifPresent(member -> {
            if (!member.getId().equals(id)) {
                throw new DuplicateEmailException(request.getEmail());
            }
        });

        var existingMember = memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);

        existingMember.setName(request.getName());
        existingMember.setEmail(request.getEmail());
        existingMember.setPhone(request.getPhone());
        existingMember.setBirthDate(request.getBirthDate());
        existingMember.setUpdatedAt(LocalDateTime.now());

        memberRepository.saveAndFlush(existingMember);
    }

    @Transactional
    public void deactivateMember(UUID id) {

        var existingMember = memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);

        existingMember.setIsActive(false);
        existingMember.setUpdatedAt(LocalDateTime.now());

        memberRepository.saveAndFlush(existingMember);
    }

    @Transactional
    public void deleteMember(UUID id) {

        var existingMember = memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);

        memberRepository.delete(existingMember);
    }
}
