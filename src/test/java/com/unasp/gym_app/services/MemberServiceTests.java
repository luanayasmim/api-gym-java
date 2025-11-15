package com.unasp.gym_app.services;

import com.unasp.gym_app.infrastructure.entities.Member;
import com.unasp.gym_app.infrastructure.repositories.IMemberRepository;
import com.unasp.gym_app.shared.dtos.MemberRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(MemberService.class)
class MemberServiceTests {

    @Autowired
    private IMemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    private MemberRequest request;

    @BeforeEach
    void setup() {
        request = new MemberRequest(
                "John Doe",
                "john.doe@example.com",
                "+12345678901",
                LocalDate.of(1990, 1, 1)
        );
    }

    @Test
    void createMember_setsCreatedAtAndIsActive_andUpdatedAtNull() {
        Member saved = memberService.createMember(request);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getIsActive()).isTrue();
        assertThat(saved.getUpdatedAt()).isNull();
    }

    @Test
    void updateMember_setsUpdatedAt() {
        Member saved = memberService.createMember(request);
        LocalDateTime originalCreatedAt = saved.getCreatedAt();
        assertThat(saved.getUpdatedAt()).isNull();

        MemberRequest updateRequest = new MemberRequest(
                "John A. Doe",
                "john.doe@example.com", // same email
                "+12345678901",
                LocalDate.of(1990, 1, 1)
        );

        memberService.updateMember(saved.getId(), updateRequest);
        Member updated = memberRepository.findById(saved.getId()).orElseThrow();

        assertThat(updated.getCreatedAt()).isEqualTo(originalCreatedAt);
        assertThat(updated.getUpdatedAt()).isNotNull();
        assertThat(updated.getName()).isEqualTo("John A. Doe");
    }

    @Test
    void deactivateMember_setsIsActiveFalse_andUpdatedAt() {
        Member saved = memberService.createMember(request);
        assertThat(saved.getIsActive()).isTrue();
        assertThat(saved.getUpdatedAt()).isNull();

        memberService.deactivateMember(saved.getId());
        Member deactivated = memberRepository.findById(saved.getId()).orElseThrow();

        assertThat(deactivated.getIsActive()).isFalse();
        assertThat(deactivated.getUpdatedAt()).isNotNull();
    }

    @Test
    void createMember_duplicateEmail_throws() {
        memberService.createMember(request);
        MemberRequest duplicate = new MemberRequest(
                "Jane Doe",
                request.getEmail(),
                "+12345678902",
                LocalDate.of(1992, 2, 2)
        );
        try {
            memberService.createMember(duplicate);
        } catch (RuntimeException ex) {
            assertThat(ex.getMessage()).contains("Email already in use");
            return;
        }
        throw new AssertionError("Expected RuntimeException for duplicate email");
    }

    @Test
    void updateMember_changeEmailToExisting_throws() {
        Member first = memberService.createMember(request);
        MemberRequest secondReq = new MemberRequest(
                "Second User",
                "second@example.com",
                "+12345678903",
                LocalDate.of(1991, 3, 3)
        );
        Member second = memberService.createMember(secondReq);

        MemberRequest attempt = new MemberRequest(
                "Second User",
                first.getEmail(), // using first's email
                "+12345678903",
                LocalDate.of(1991, 3, 3)
        );

        try {
            memberService.updateMember(second.getId(), attempt);
        } catch (RuntimeException ex) {
            assertThat(ex.getMessage()).contains("Email already in use");
            return;
        }
        throw new AssertionError("Expected RuntimeException when changing email to one already in use");
    }
}

