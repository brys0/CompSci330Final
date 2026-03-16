package org.uwgb.compsci330.server.config;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.uwgb.compsci330.server.entity.user.User;
import org.uwgb.compsci330.server.repository.UserRepository;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;


    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.existsByUsername("System")) return;

        userRepository.save(User.createSystemUser());
    }
}
