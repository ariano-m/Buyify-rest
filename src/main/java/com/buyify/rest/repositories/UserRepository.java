package com.buyify.rest.repositories;

import com.buyify.rest.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    User save(User user);

    @Transactional(readOnly = true)
    Optional<User> findByUsername(String username);

    @Transactional(readOnly = true)
    Optional<User> findByName(String name);

}