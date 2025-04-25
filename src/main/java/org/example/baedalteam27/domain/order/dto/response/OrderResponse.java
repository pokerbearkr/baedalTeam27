package org.example.baedalteam27.domain.order.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.baedalteam27.domain.order.entity.OrderStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class OrderResponse {
    private Long orderId;

    private String location;

    private LocalDateTime orderedTime;

    private OrderStatus orderStatus;
}
