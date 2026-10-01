package com.trainosys.shopapi.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(long id);
    Optional<User> findByEmail(String email);
    boolean deleteByUserId(Long userId);
}
