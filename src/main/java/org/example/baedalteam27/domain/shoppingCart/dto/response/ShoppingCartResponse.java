package org.example.baedalteam27.domain.shoppingCart.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ShoppingCartResponse {
    private final Menu menu;
    private final int quantity;
}