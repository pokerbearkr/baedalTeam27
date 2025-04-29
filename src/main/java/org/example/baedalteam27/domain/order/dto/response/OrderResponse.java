package org.example.baedalteam27.domain.order.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.baedalteam27.domain.order.entity.OrderStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class OrderResponse {
    private final Long orderId;

    private final String location;

    private final LocalDateTime orderedTime;

    private final OrderStatus orderStatus;
}
