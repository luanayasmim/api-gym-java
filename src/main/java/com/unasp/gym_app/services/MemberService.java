package com.unasp.gym_app.services;

import com.unasp.gym_app.infrastructure.entities.Member;
import com.unasp.gym_app.infrastructure.repositories.IMemberRepository;
import com.unasp.gym_app.shared.dtos.MemberRequest;
import com.unasp.gym_app.shared.exceptions.DuplicateEmailException;
import com.unasp.gym_app.shared.exceptions.MemberNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class MemberService {
    @Autowired
    private IMemberRepository memberRepository;

    public Member getMemberById(UUID id) {
        return memberRepository.findById(id).orElse(null);
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

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
                .build();

        return memberRepository.saveAndFlush(member);
    }

    public void updateMember(UUID id, MemberRequest request) {
        var existingMemberByEmail = memberRepository.findByEmail(request.getEmail());

        if (existingMemberByEmail != null && !existingMemberByEmail.getId().equals(id)) {
            throw new DuplicateEmailException(request.getEmail());
        }

        var existingMember = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);

        existingMember.setName(request.getName());
        existingMember.setEmail(request.getEmail());
        existingMember.setPhone(request.getPhone());
        existingMember.setBirthDate(request.getBirthDate());
        existingMember.setUpdatedAt(LocalDateTime.now());

        memberRepository.saveAndFlush(existingMember);
    }

    public void deactivateMember(UUID id) {
        var existingMember = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);

        existingMember.setIsActive(false);
        existingMember.setUpdatedAt(LocalDateTime.now());

        memberRepository.saveAndFlush(existingMember);
    }

    public void deleteMember(UUID id) {
        memberRepository.deleteById(id);
    }
}
