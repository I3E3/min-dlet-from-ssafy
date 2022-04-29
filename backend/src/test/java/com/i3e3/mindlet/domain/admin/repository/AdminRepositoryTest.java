package com.i3e3.mindlet.domain.admin.repository;

import com.i3e3.mindlet.domain.admin.entity.Admin;
import com.i3e3.mindlet.global.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
class AdminRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private AdminRepository adminRepository;

    private Admin admin1;

    @BeforeEach
    void setUp() {
        adminRepository.deleteAll();
        em.flush();
        em.clear();

        admin1 = Admin.builder()
                .id("id01")
                .password("pass12#$")
                .role(Role.ADMIN)
                .build();
    }
}