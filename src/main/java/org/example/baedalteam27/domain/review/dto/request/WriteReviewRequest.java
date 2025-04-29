package org.example.baedalteam27.domain.review.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.baedalteam27.domain.order.entity.Order;
import org.example.baedalteam27.domain.store.entity.Store;

@Getter
@AllArgsConstructor
public class WriteReviewRequest {
    float starScore;

    String ReviewText;

    String ImgURL;
}
