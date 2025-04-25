package org.example.baedalteam27.domain.shoppingCart.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddShoppingCartRequest {
    Long user;
    Long store;
    Long menu;
    int quantity;
}
