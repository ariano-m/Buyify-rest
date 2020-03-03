package com.buyify.rest.repositories;

import com.buyify.rest.entities.Order;
import com.buyify.rest.entities.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByUser(User user);
}