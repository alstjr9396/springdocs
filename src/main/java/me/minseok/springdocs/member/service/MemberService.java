package me.minseok.springdocs.member.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import me.minseok.springdocs.member.domain.Member;
import me.minseok.springdocs.member.domain.repository.MemberRepository;
import me.minseok.springdocs.member.dto.MemberRequest;
import me.minseok.springdocs.member.dto.MemberResponse;
import me.minseok.springdocs.global.exception.BadRequestException;
import me.minseok.springdocs.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public Long saveMember(MemberRequest memberRequest) {
        Member member = Member.of(memberRequest);
        validateMemberDuplicate(member);
        Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }

    private void validateMemberDuplicate(Member member) {
        validateMemberDuplicatedName(member.getName());
    }

    private void validateMemberDuplicatedName(String name) {
        if (memberRepository.existsByName(name)) {
            throw new IllegalArgumentException("Duplicated member name");
        }
    }

    public void deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new BadRequestException(ErrorCode.INVALID_MEMBER_ID));

        //외래키 확인

        memberRepository.delete(member);
    }

    @Transactional(readOnly = true)
    public List<MemberResponse> getAllMembers() {
        List<Member> allMembers = memberRepository.findAll();
        return allMembers.stream()
                .map(MemberResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MemberResponse getMember(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new BadRequestException(ErrorCode.INVALID_MEMBER_ID));
        return MemberResponse.of(member);
    }
}
