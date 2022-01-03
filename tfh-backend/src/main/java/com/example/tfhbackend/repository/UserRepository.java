package com.example.tfhbackend.repository;

import com.example.tfhbackend.model.User;
import com.example.tfhbackend.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhone(String phone);
    Optional<User> findByEmail(String email);
    long countAllByRole(UserRole role);
}
