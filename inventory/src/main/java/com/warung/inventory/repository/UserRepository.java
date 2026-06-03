package com.warung.inventory.repository;

import java.util.UUID;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warung.inventory.model.User;

public interface UserRepository extends JpaRepository<User, UUID> {

    public Optional<User> findByEmail(String email);
}