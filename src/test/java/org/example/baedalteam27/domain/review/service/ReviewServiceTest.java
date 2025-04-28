package org.example.baedalteam27.domain.review.service;


import org.example.baedalteam27.domain.order.entity.Order;
import org.example.baedalteam27.domain.order.repository.OrderRepository;
import org.example.baedalteam27.domain.review.dto.request.ReviseReviewRequest;
import org.example.baedalteam27.domain.review.dto.request.WriteReviewRequest;
import org.example.baedalteam27.domain.review.dto.response.ReviewResponse;
import org.example.baedalteam27.domain.review.entity.Review;
import org.example.baedalteam27.domain.review.repository.ReviewRepository;
import org.example.baedalteam27.domain.store.entity.Store;
import org.example.baedalteam27.domain.user.entitiy.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    void write_성공() {
        // given
        Long orderId = 1L;
        Order order = mock(Order.class);
        given(orderRepository.findByIdOrElseThrow(orderId)).willReturn(order);

        WriteReviewRequest request = new WriteReviewRequest(5, "맛있어요", "imgUrl");

        // when
        reviewService.write(orderId, request);

        // then
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void revise_성공() {
        // given
        Long userId = 1L;
        Long reviewId = 2L;
        Review review = mock(Review.class);
        User user = mock(User.class);
        given(reviewRepository.findByIdOrElseThrow(reviewId)).willReturn(review);
        given(review.getUser()).willReturn(user);
        given(user.getId()).willReturn(userId);

        ReviseReviewRequest request = new ReviseReviewRequest(reviewId, "수정된 리뷰");

        // when
        reviewService.revise(userId, request);

        // then
        verify(review, times(1)).setReviewText("수정된 리뷰");
    }

    @Test
    void revise_실패_타인리뷰수정() {
        // given
        Long loginUserId = 1L;
        Long reviewId = 2L;
        Review review = mock(Review.class);
        User user = mock(User.class);
        given(reviewRepository.findByIdOrElseThrow(reviewId)).willReturn(review);
        given(review.getUser()).willReturn(user);
        given(user.getId()).willReturn(999L); // 다른 사람 ID

        ReviseReviewRequest request = new ReviseReviewRequest(reviewId, "수정된 리뷰");

        // when & then
        assertThrows(IllegalArgumentException.class, () -> reviewService.revise(loginUserId, request));
    }

    @Test
    void delete_성공() {
        // given
        Long userId = 1L;
        Long reviewId = 2L;
        Review review = mock(Review.class);
        User user = mock(User.class);
        given(reviewRepository.findByIdOrElseThrow(reviewId)).willReturn(review);
        given(review.getUser()).willReturn(user);
        given(user.getId()).willReturn(userId);

        // when
        reviewService.delete(userId, reviewId);

        // then
        verify(reviewRepository, times(1)).delete(review);
    }

    @Test
    void delete_실패_타인리뷰삭제() {
        // given
        Long loginUserId = 1L;
        Long reviewId = 2L;
        Review review = mock(Review.class);
        User user = mock(User.class);
        given(reviewRepository.findByIdOrElseThrow(reviewId)).willReturn(review);
        given(review.getUser()).willReturn(user);
        given(user.getId()).willReturn(999L); // 다른 사람 ID

        // when & then
        assertThrows(IllegalArgumentException.class, () -> reviewService.delete(loginUserId, reviewId));
    }

    @Test
    void getAllReviewsAboutStore_성공() {
        // given
        Long storeId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        Review review = mock(Review.class);
        Store store = mock(Store.class);
        User user = mock(User.class);

        given(store.getId()).willReturn(1L);
        given(user.getId()).willReturn(1L);
        given(review.getStore()).willReturn(store);
        given(review.getUser()).willReturn(user);
        given(review.getStarScore()).willReturn(5f);
        given(review.getReviewText()).willReturn("맛있다");
        given(review.getImgURL()).willReturn("imgUrl");

        Page<Review> page = new PageImpl<>(List.of(review));
        given(reviewRepository.findByStoreId(storeId, pageable)).willReturn(page);

        // when
        Page<ReviewResponse> result = reviewService.getAllReviewsAboutStore(storeId, pageable);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getReviewText()).isEqualTo("맛있다");
    }
}