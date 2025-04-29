package org.example.baedalteam27.domain.shoppingCart.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddShoppingCartRequest {
    private final Long user;
    private final Long store;
    private final Long menu;
    private final int quantity;
}
