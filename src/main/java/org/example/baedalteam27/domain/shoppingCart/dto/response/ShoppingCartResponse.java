package org.example.baedalteam27.domain.shoppingCart.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.baedalteam27.domain.user.entitiy.User;
import org.example.baedalteam27.domain.store.entity.Store;
import org.example.baedalteam27.domain.menu.entity.Menu;

@Getter
@AllArgsConstructor
public class ShoppingCartResponse {
    private final String store;
    private final String menu;
    private final int price;
    private final int quantity;
}