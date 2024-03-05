package me.minseok.springdocs.member.service;

import static me.minseok.springdocs.member.fixture.TestMember.MEMBER;
import static me.minseok.springdocs.member.fixture.TestMember.MEMBERS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import me.minseok.springdocs.global.exception.BadRequestException;
import me.minseok.springdocs.global.exception.ErrorCode;
import me.minseok.springdocs.member.domain.Member;
import me.minseok.springdocs.member.domain.repository.MemberRepository;
import me.minseok.springdocs.member.dto.MemberRequest;
import me.minseok.springdocs.member.dto.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @DisplayName("새로운 멤버를 추가한다.")
    @Test
    void saveMember() {
        // given
        MemberRequest memberRequest = new MemberRequest(1L, MEMBER.getName());

        given(memberRepository.existsByName(anyString())).willReturn(false);
        given(memberRepository.save(any(Member.class))).willReturn(MEMBER);

        // when
        Long actualId = memberService.saveMember(memberRequest);

        // then
        assertThat(actualId).isEqualTo(MEMBER.getId());
    }

    @DisplayName("중복된 이름의 멤버를 추가할 수 없다.")
    @Test
    void saveMember_duplicateFailByName() {
        // given
        MemberRequest memberRequest = new MemberRequest(1L, MEMBER.getName());

        given(memberRepository.existsByName(anyString())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> memberService.saveMember(memberRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("특정 멤버의 정보를 반환한다.")
    @Test
    void getMember() {
        // given
        given(memberRepository.findById(1L))
                .willReturn(Optional.of(MEMBER));

        // when
        MemberResponse actualResponses = memberService.getMember(1L);

        // then
        assertThat(actualResponses).usingRecursiveComparison().isEqualTo(MEMBER);
    }

    @DisplayName("ID에 해당하는 멤버가 존재하지 않으면 예외가 발생한다.")
    @Test
    void getMember_NotFoundFail() {
        // given
        given(memberRepository.findById(9999L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> memberService.getMember(9999L))
                .isInstanceOf(BadRequestException.class);
    }

    @DisplayName("모든 멤버의 정보를 반환한다.")
    @Test
    void getAllMember() {
        // given
        final List<MemberResponse> responses = MEMBERS.stream()
                .map(MemberResponse::of)
                .collect(Collectors.toList());

        given(memberRepository.findAll())
                .willReturn(MEMBERS);

        // when
        final List<MemberResponse> actualResponses = memberService.getAllMembers();

        // then
        assertThat(actualResponses).usingRecursiveComparison().isEqualTo(responses);
    }

    @DisplayName("유효하지 않은 memberId를 삭제할 시 예외가 발생한다.")
    @Test
    void delete_InvalidMemberId() {
        // given
        final Long invalidMemberId = 9999L;

        given(memberRepository.findById(invalidMemberId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> memberService.deleteMember(invalidMemberId))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_MEMBER_ID);
    }
}