package com.i3e3.mindlet.domain.admin.repository;

import com.i3e3.mindlet.domain.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    boolean existsById(String id);
}
