package org.example.baedalteam27.domain.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ReviewResponse {
    private final Long storeId;
    private final Long userId;
    private final float starScore;
    private final String reviewText;
    private final String imgURL;
}
