package me.minseok.springdocs.member.fixture;

import java.util.List;
import me.minseok.springdocs.member.domain.Member;
import me.minseok.springdocs.member.domain.StatusType;

public class TestMember {

    public static final List<Member> MEMBERS = List.of(
            new Member(1L, "gildong", StatusType.USED),
            new Member(2L, "minsu", StatusType.USED)
    );

    public static final Member MEMBER = MEMBERS.get(0);
}
