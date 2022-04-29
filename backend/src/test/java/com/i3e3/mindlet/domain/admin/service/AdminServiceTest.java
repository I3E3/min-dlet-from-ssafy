package com.i3e3.mindlet.domain.admin.service;

import com.i3e3.mindlet.domain.admin.controller.form.RegisterForm;
import com.i3e3.mindlet.domain.admin.repository.AdminRepository;
import com.i3e3.mindlet.domain.admin.repository.RegisterKeyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
class AdminServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RegisterKeyRepository registerKeyRepository;

    @Autowired
    private AdminService adminService;

    private RegisterForm registerForm1;

    @BeforeEach
    void setUp() {
        adminRepository.deleteAll();
        registerKeyRepository.deleteAll();
        em.flush();
        em.clear();

        registerForm1 = RegisterForm.builder()
                .id("id01")
                .password("password01")
                .key("aaaa-bbbb-cccc-dddd")
                .build();
    }
}