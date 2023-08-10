package com.mss.ged.repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mss.ged.entities.User;
 

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);
  User findUserByUsername(String username);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);
}