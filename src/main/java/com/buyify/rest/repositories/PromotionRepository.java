package com.buyify.rest.repositories;

import com.buyify.rest.entities.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    Promotion findByProductId(Long id);

}

