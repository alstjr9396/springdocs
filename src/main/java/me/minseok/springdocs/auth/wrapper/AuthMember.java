package me.minseok.springdocs.auth.wrapper;


import lombok.AllArgsConstructor;
import lombok.Getter;
import me.minseok.springdocs.auth.Authority;

@Getter
@AllArgsConstructor
public class AuthMember {

    private Long memberId;
    private Authority authority;

    public static AuthMember member(Long memberId) {
        return new AuthMember(memberId, Authority.MEMBER);
    }

    public static AuthMember admin(Long memberId) {
        return new AuthMember(memberId, Authority.ADMIN);
    }

    public boolean isMember() {
        return Authority.MEMBER.equals(authority);
    }

    public boolean isAdmin() {
        return Authority.ADMIN.equals(authority);
    }
}
