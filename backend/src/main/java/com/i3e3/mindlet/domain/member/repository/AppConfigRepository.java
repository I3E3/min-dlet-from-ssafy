package com.i3e3.mindlet.domain.member.repository;

import com.i3e3.mindlet.domain.member.entity.AppConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppConfigRepository extends JpaRepository<AppConfig, Long> {
}
