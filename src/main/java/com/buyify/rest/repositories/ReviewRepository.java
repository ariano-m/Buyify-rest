package com.buyify.rest.repositories;

import com.buyify.rest.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    Review save(Review review);

}