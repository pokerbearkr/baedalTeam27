package org.example.baedalteam27.domain.menu.dto;

import lombok.Getter;

@Getter
public class MenuDto {
    private final Long storeId;
    private final String name;
    private final int price;
    private final String description;
    private final boolean isSoldOut;

    public MenuDto(Long storeId, String name, int price, String description, boolean isSoldOut) {
        this.storeId = storeId;
        this.name = name;
        this.price = price;
        this.description = description;
        this.isSoldOut = isSoldOut;
    }
}
