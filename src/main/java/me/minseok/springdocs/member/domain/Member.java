package me.minseok.springdocs.member.domain;


import static javax.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;
import static me.minseok.springdocs.member.domain.StatusType.USED;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.minseok.springdocs.member.dto.MemberRequest;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;


@SQLDelete(sql = "UPDATE member SET status = 'DELETED' WHERE id = ?")
@Where(clause = "status = 'USED'")
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Getter
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(value = STRING)
    private StatusType status = USED;

    public Member(String name) {
        this.name = name;
    }

    public static Member of(MemberRequest memberRequest) {
        return new Member(memberRequest.getName());
    }
}
