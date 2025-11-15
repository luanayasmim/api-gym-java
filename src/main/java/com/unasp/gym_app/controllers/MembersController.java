package com.unasp.gym_app.controllers;

import com.unasp.gym_app.infrastructure.entities.Member;
import com.unasp.gym_app.services.MemberService;
import com.unasp.gym_app.shared.dtos.MemberRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/members")
public class MembersController {

    @Autowired
    private MemberService service;

    @GetMapping("/{id}")
    public ResponseEntity<Member> getMember(@PathVariable UUID id){
        var response = service.getMemberById(id);

        if (response == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Member>> getAllMembers() {
        var response = service.getAllMembers();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Member> createMember(@Valid @RequestBody MemberRequest request) {
        var response = service.createMember(request);

        var location = URI.create(String.format("/members/%s", response));

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Member> updateMember(@PathVariable UUID id, @Valid @RequestBody MemberRequest request) {
        service.updateMember(id, request);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateMember(@PathVariable UUID id) {
        service.deactivateMember(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable UUID id) {
        service.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

}
