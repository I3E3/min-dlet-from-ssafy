package com.i3e3.mindlet.domain.dandelion.repository;

import com.i3e3.mindlet.domain.dandelion.entity.Dandelion;
import com.i3e3.mindlet.domain.member.entity.Member;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.i3e3.mindlet.domain.dandelion.entity.QDandelion.dandelion;
import static com.i3e3.mindlet.domain.dandelion.entity.QPetal.petal;
import static com.i3e3.mindlet.domain.member.entity.QMemberDandelionHistory.memberDandelionHistory;

public class DandelionRepositoryImpl implements DandelionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public DandelionRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public int countUsingSeed(Long memberSeq) {
        JPAQuery<Integer> query = queryFactory
                .select(dandelion.count().intValue())
                .from(dandelion)
                .where(dandelion.member.seq.eq(memberSeq)
                        .and(dandelion.isDeleted.isFalse()));

        return query.where(dandelion.status.in(
                        Dandelion.Status.FLYING,
                        Dandelion.Status.HOLD,
                        Dandelion.Status.BLOSSOMED,
                        Dandelion.Status.PENDING,
                        Dandelion.Status.RETURN))
                .fetchFirst();
    }

    @Override
    public List<Dandelion> findActiveDandelionListByMemberSeq(Long memberSeq) {
        JPAQuery<Dandelion> query = queryFactory
                .select(dandelion)
                .from(dandelion)
                .where(dandelion.member.seq.eq(memberSeq)
                        .and(dandelion.member.isDeleted.isFalse())
                        .and(dandelion.isDeleted.isFalse()));

        return query.where(dandelion.status.in(
                        Dandelion.Status.FLYING,
                        Dandelion.Status.HOLD,
                        Dandelion.Status.BLOSSOMED,
                        Dandelion.Status.PENDING,
                        Dandelion.Status.RETURN))
                .fetch();
    }

    @Override
    public Optional<Dandelion> findRandomFlyingDandelionExceptMember(Member member) {
        /*
        SELECT *
        FROM tb_dandelion d
        LEFT JOIN tb_member_dandelion_history md on d.dandelion_seq = md.dandelion_seq
        WHERE
            d.status = 'FLYING' AND
            d.community = 'KOREA' AND // <- KOREA : 회원의 커뮤니티
            d.is_deleted = false AND
            d.member_seq != 2 AND // <- 2 : 회원의 식별키
            md.member_seq is null
        ORDER BY d.last_modified_date ASC
        LIMIT 1;
         */
        return Optional.ofNullable(queryFactory
                .selectFrom(dandelion)
                .leftJoin(memberDandelionHistory).on(memberDandelionHistory.dandelion.eq(dandelion))
                .where(
                        dandelion.status.eq(Dandelion.Status.FLYING),
                        dandelion.community.eq(member.getAppConfig().getCommunity()),
                        dandelion.isDeleted.isFalse(),
                        dandelion.member.ne(member),
                        memberDandelionHistory.member.isNull())
                .orderBy(dandelion.lastModifiedDate.asc())
                .limit(1L)
                .fetchOne());
    }

    @Override
    public void updateHoldingDandelionToFlying(long elapsedMinute) {
        queryFactory
                .update(dandelion)
                .set(dandelion.status, Dandelion.Status.FLYING)
                .where(
                        dandelion.status.eq(Dandelion.Status.HOLD),
                        dandelion.lastModifiedDate.before(LocalDateTime.now().minusMinutes(elapsedMinute)),
                        dandelion.isDeleted.isFalse())
                .execute();
    }

    @Override
    public long countParticipationDandelions(Long memberSeq) {

        return queryFactory
                .select(petal.count())
                .from(petal)
                .where(
                        petal.member.seq.eq(memberSeq),
                        petal.dandelion.member.seq.ne(memberSeq),
                        petal.isDeleted.isFalse(),
                        petal.dandelion.isDeleted.isFalse(),
                        petal.dandelion.member.isDeleted.isFalse(),
                        petal.dandelion.status.in(
                                Dandelion.Status.BLOSSOMED,
                                Dandelion.Status.ALBUM
                        )
                )
                .fetchFirst();
    }

    @Override
    public Optional<List<Dandelion>> findParticipationByMemberSeqAndPageable(Long memberSeq, Pageable pageable) {

        List<Dandelion> dandelions = queryFactory
                .selectFrom(dandelion)
                .where(
                        dandelion.seq.in(
                                JPAExpressions.select(petal.dandelion.seq)
                                        .from(petal)
                                        .where(petal.member.seq.eq(memberSeq),
                                                petal.isDeleted.isFalse()
                                        )
                        ),
                        dandelion.member.seq.ne(memberSeq),
                        dandelion.isDeleted.isFalse(),
                        dandelion.member.isDeleted.isFalse(),
                        dandelion.status.in(
                                Dandelion.Status.BLOSSOMED,
                                Dandelion.Status.ALBUM
                        )
                )
                .offset((long) (pageable.getPageNumber() - 1) * pageable.getPageSize())
                .limit(pageable.getPageSize())
                .orderBy(dandelion.blossomedDate.desc())
                .fetch();
        if (dandelions.size() == 0) {
            dandelions = null;
        }

        return Optional.ofNullable(dandelions);
    }
}
