package me.minseok.springdocs.member.domain.repository;

import java.util.List;
import me.minseok.springdocs.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findAll();

    boolean existsByName(String name);
}
