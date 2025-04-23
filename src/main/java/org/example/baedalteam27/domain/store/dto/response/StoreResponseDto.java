package org.example.baedalteam27.domain.store.dto.response;

import lombok.Getter;

import java.awt.*;
import java.time.LocalTime;
import java.util.List;

@Getter
public class StoreResponseDto {
    private final Long id;
    private final String storeName;
    private final String address;
    private final String phoneNumber;
    private final LocalTime openTime;
    private final LocalTime closedTime;
    private final Long minOrderPrice;
    private final List<MenuResponseDto> menus;


    public StoreResponseDto(Long id, String storeName, String address, String phoneNumber, LocalTime openTime, LocalTime closedTime, Long minOrderPrice, List<MenuResponseDto> menus) {
        this.id = id;
        this.storeName = storeName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.openTime = openTime;
        this.closedTime = closedTime;
        this.minOrderPrice = minOrderPrice;
        this.menus = menus;
    }
}
