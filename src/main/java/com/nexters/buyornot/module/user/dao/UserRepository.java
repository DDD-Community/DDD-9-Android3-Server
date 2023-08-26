package com.nexters.buyornot.module.user.dao;

import com.nexters.buyornot.module.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    User findByNickname(String nickname);

    boolean existsByNickname(String nickname);
}
