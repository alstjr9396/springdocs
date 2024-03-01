package me.minseok.springdocs.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.minseok.springdocs.member.domain.Member;

@AllArgsConstructor
@Getter
public class MemberResponse {

    private Long id;
    private String name;

    public static MemberResponse of(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getName()
        );
    }
}
