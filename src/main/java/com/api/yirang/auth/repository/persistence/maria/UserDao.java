package com.api.yirang.auth.repository.persistence.maria;

import com.api.yirang.auth.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserDao extends JpaRepository<User, Long> {
    Optional<User> findByUserId(Long userId);
}
