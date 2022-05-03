package com.i3e3.mindlet.domain.dandelion.repository;

import com.i3e3.mindlet.domain.dandelion.entity.Dandelion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DandelionRepository extends JpaRepository<Dandelion, Long>, DandelionRepositoryCustom {

    @Query("SELECT d FROM Dandelion d WHERE d.seq = :seq AND d.isDeleted = FALSE")
    Optional<Dandelion> findBySeq(@Param("seq") Long seq);
}
