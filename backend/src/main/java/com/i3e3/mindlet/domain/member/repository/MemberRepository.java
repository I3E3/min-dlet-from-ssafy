package com.i3e3.mindlet.domain.member.repository;

import com.i3e3.mindlet.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
