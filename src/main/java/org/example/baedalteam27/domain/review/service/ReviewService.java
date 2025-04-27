package org.example.baedalteam27.domain.review.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.baedalteam27.domain.order.entity.Order;
import org.example.baedalteam27.domain.order.repository.OrderDetailsRepository;
import org.example.baedalteam27.domain.order.repository.OrderRepository;
import org.example.baedalteam27.domain.review.dto.request.ReviseReviewRequest;
import org.example.baedalteam27.domain.review.dto.request.WriteReviewRequest;
import org.example.baedalteam27.domain.review.dto.response.ReviewResponse;
import org.example.baedalteam27.domain.review.entity.Review;
import org.example.baedalteam27.domain.review.repository.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public void write(Long orderId, WriteReviewRequest dto){
        Order myOrder = orderRepository.findByIdOrElseThrow(orderId);

        Review review = Review.builder()
                .starScore(dto.getStarScore())
                .reviewText(dto.getReviewText())
                .ImgURL(dto.getImgURL())
                .store(myOrder.getStore())
                .user(myOrder.getUser())
                .order(myOrder)
                .build();
        reviewRepository.save(review);
    }

    @Transactional
    public void revise(Long userId, ReviseReviewRequest dto){
        Review review = reviewRepository.findByIdOrElseThrow(dto.getReviewId());

        // 본인이 쓴 리뷰가 맞는지 확인
        isThisYourReview(userId, review.getUser().getId());

        //수정 & 저장
        review.setReviewText(dto.getReviewText());
    }

    @Transactional
    public void delete(Long userid, Long reviewId){
        Review review = reviewRepository.findByIdOrElseThrow(reviewId);

        isThisYourReview(userid, review.getUser().getId());

        reviewRepository.delete(review);
    }

    public List<ReviewResponse> getAllReviewsAboutStore(Long storeId, Pageable pageable) {
        Page<Review> storeReview = reviewRepository.findByStoreId(storeId, pageable);

        return storeReview.stream()
                .map(s -> new ReviewResponse(
                        s.getStore().getId(),
                        s.getUser().getId(),
                        s.getStarScore(),
                        s.getReviewText(),
                        s.getImgURL()
                ))
                .toList();
    }

    public void isThisYourReview(Long loginUserId, Long selectUserId){
        if(!loginUserId.equals(selectUserId)){
            throw new IllegalArgumentException("타인의 리뷰는 변경할 수 없습니다.");
        }
    }
}
