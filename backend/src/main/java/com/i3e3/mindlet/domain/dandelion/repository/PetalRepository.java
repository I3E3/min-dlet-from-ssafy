package com.i3e3.mindlet.domain.dandelion.repository;

import com.i3e3.mindlet.domain.dandelion.entity.Dandelion;
import com.i3e3.mindlet.domain.dandelion.entity.Petal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PetalRepository extends JpaRepository<Petal, Long> {

    @Query("SELECT p FROM Petal p WHERE p.seq = :seq AND p.isDeleted = FALSE")
    Optional<Petal> findBySeq(@Param("seq") Long seq);

    @Query("SELECT p.dandelion FROM Petal p WHERE p.seq = :seq AND p.isDeleted = FALSE")
    Optional<Dandelion> findDandelionBySeq(@Param("seq") Long seq);
}
