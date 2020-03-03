package com.buyify.rest.repositories;

import com.buyify.rest.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByName(String name);

}