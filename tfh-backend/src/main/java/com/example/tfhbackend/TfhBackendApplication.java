package com.example.tfhbackend;

import com.example.tfhbackend.model.User;
import com.example.tfhbackend.model.enums.UserRole;
import com.example.tfhbackend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@SpringBootApplication
@EnableJpaRepositories
public class TfhBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(TfhBackendApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(UserRepository userRepository,
                                  BCryptPasswordEncoder passwordEncoder) {
        return args -> {
            addDefaultAdminUser(userRepository, passwordEncoder);
        };
    }

    private void addDefaultAdminUser(UserRepository userRepository,
                                     BCryptPasswordEncoder passwordEncoder) {
        Optional<User> optionalUser = userRepository.findByPhone("+37369999999");
        if (optionalUser.isEmpty()) {
            User user = User.builder()
                    .firstName("ADMIN")
                    .lastName("ADMIN")
                    .phone("+37369999999")
                    .email("admin@gmail.com")
                    .password(passwordEncoder.encode("password"))
                    .role(UserRole.ADMIN)
                    .confirmed(true)
                    .build();
            userRepository.save(user);
        }
    }
}
