package org.example.baedalteam27.domain.review.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviseReviewRequest {
    private final Long reviewId;
    private final String reviewText;
}
