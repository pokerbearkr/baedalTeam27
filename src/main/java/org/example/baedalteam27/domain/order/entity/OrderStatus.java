package org.example.baedalteam27.domain.order.entity;

import lombok.Getter;

@Getter
public enum OrderStatus {
    //판매자
    ACCEPTED(1, "주문 승인"),
    REJECTED(2, "주문 거절"),

    //소비자
    PENDING(3, "주문 요청중"),
    CANCELED(4, "주문 취소");

    private final int code;
    private final String description;

    OrderStatus(int code, String description){
        this.code = code;
        this.description = description;
    }
}
