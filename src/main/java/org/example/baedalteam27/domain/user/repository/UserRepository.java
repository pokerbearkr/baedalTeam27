package org.example.baedalteam27.domain.user.repository;

import java.util.Optional;

import org.example.baedalteam27.domain.user.entitiy.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
	boolean existsByEmail(String email);
}