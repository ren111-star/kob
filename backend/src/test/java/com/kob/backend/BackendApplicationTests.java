package com.kob.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class BackendApplicationTests {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Test
    void contextLoads() {
        System.out.println(passwordEncoder.encode("pyxc"));
    }

}
