package org.example.baedalteam27.domain.order.dto.request;

import lombok.Getter;

@Getter
public class OrderRequestDto {
    private final String deliveryLocation;

    public OrderRequestDto(String deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }
}
