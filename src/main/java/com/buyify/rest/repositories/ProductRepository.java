package com.buyify.rest.repositories;

import com.buyify.rest.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    Product save(Product product);

    @Transactional(readOnly = true)
    List<Product> findAll();

    @Transactional(readOnly = true)
    Optional<Product> findById(Long id);

    @Transactional(readOnly = true)
    Product findByName(String name);

    @Transactional(readOnly = true)
    @Query("SELECT p FROM Product p WHERE lower(p.name) LIKE lower(concat('%', ?1,'%'))")
    List<Product> findByNameIsLike(String name);

}