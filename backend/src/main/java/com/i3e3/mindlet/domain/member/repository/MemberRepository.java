package com.i3e3.mindlet.domain.member.repository;

import com.i3e3.mindlet.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("SELECT COUNT(m.seq) > 0 FROM Member m WHERE m.seq = :seq AND m.isDeleted = false")
    boolean existsBySeq(@Param("seq") Long seq);

    @Query("SELECT COUNT(m) > 0 FROM Member m WHERE m.id = :id")
    boolean existsByIdContainsDeleted(@Param("id") String id);
}