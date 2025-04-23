package org.example.baedalteam27.domain.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderItmesResponse {
    private final Menu menu;
    private final int quantity;
}
