package org.uwgb.compsci330.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.uwgb.compsci330.server.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String username);

    // TODO: Use Optional return instead of list 
    Optional<User> findByUsername(String username);

    // TODO: Use Optional return instead of list
    Optional<User> findUserById(String id);
}
