package org.uwgb.compsci330.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.uwgb.compsci330.server.entity.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    public boolean existsByUsername(String username);
    public List<User> findByUsername(String username);

    public List<User> findUserById(String id);
}
