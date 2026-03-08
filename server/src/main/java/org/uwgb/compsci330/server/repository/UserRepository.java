package org.uwgb.compsci330.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.uwgb.compsci330.server.entity.user.User;
import org.uwgb.compsci330.server.entity.user.UserStatus;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String username);

    @Modifying
    @Query("UPDATE users SET status = :status WHERE id = :userId")
    void setUserStatus(String userId, UserStatus status);

    Optional<User> findByUsername(String username);

    Optional<User> findUserById(String id);
}
