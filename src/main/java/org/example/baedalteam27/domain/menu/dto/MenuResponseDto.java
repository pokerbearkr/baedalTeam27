package org.example.baedalteam27.domain.menu.dto;

import lombok.Getter;

@Getter
public class MenuResponseDto {
    private final Long id;
    private final Long storeId;
    private final String name;
    private final int price;
    private final String description;
    private final boolean isSoldOut;


    public MenuResponseDto(Long id, Long storeId, String name, int price, String description, boolean isSoldOut) {
        this.id = id;
        this.storeId = storeId;
        this.name = name;
        this.price = price;
        this.description = description;
        this.isSoldOut = isSoldOut;
    }
}
