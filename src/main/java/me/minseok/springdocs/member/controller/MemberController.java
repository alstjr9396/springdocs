package me.minseok.springdocs.member.controller;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.minseok.springdocs.member.dto.MemberRequest;
import me.minseok.springdocs.member.dto.MemberResponse;
import me.minseok.springdocs.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping()
    ResponseEntity<MemberResponse> saveMember(@RequestBody @Valid MemberRequest memberRequest) {
        Long memberId = memberService.saveMember(memberRequest);
        URI location = URI.create("/members/" + memberId);
        return ResponseEntity.created(location).build();
    }

    @PostMapping("/{memberId}")
    ResponseEntity<Void> deleteMember(@PathVariable Long memberId) {
        memberService.deleteMember(memberId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    ResponseEntity<List<MemberResponse>> gerAllMembers() {
        List<MemberResponse> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

    @GetMapping(value = "/{memberId}")
    ResponseEntity<MemberResponse> getMember(@PathVariable Long memberId) {
        MemberResponse member = memberService.getMember(memberId);
        return ResponseEntity.ok(member);
    }
}
