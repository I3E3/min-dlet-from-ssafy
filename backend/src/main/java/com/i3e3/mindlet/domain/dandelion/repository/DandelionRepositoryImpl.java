package com.i3e3.mindlet.domain.dandelion.repository;

import com.i3e3.mindlet.domain.dandelion.entity.Dandelion;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.i3e3.mindlet.domain.dandelion.entity.QDandelion.dandelion;

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
                .fetchOne();
    }

    @Override
    public List<Dandelion> findDandelionListByMemberSeq(Long memberSeq) {
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
}
