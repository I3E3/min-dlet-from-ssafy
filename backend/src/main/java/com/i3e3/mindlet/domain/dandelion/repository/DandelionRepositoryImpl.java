package com.i3e3.mindlet.domain.dandelion.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

public class DandelionRepositoryImpl implements DandelionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public DandelionRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
}
