package org.example.baedalteam27.domain.review.repository;

import org.example.baedalteam27.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    default Review findByIdOrElseThrow(Long reviewId){
        return findById(reviewId).orElseThrow(()->new RuntimeException("리뷰가 없습니다."));
    }

    @Query("SELECT r FROM Review r JOIN FETCH r.store RIGHT JOIN OrderDetails o ON r.order.id = o.order.id WHERE r.store.id = :storeId")
    Page<Review> findByStoreId(@Param("storeId") Long storeId, Pageable page);
}
