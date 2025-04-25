package org.example.baedalteam27.domain.store.dto.response;

import lombok.Getter;
import org.example.baedalteam27.domain.menu.dto.MenuDto;
import org.example.baedalteam27.domain.menu.dto.MenuResponseDto;

import java.time.LocalTime;
import java.util.List;

@Getter
public class StoreResponseDto {
    private final Long id;
    private final String storeName;
    private final String categoryName;
    private final String address;
    private final String phoneNumber;
    private final LocalTime openTime;
    private final LocalTime closedTime;
    private final Long minOrderPrice;
    private final String status;
    private final List<MenuResponseDto> menus;


    public StoreResponseDto(Long id, String storeName, String categoryName, String address, String phoneNumber, LocalTime openTime, LocalTime closedTime, Long minOrderPrice, String status, List<MenuResponseDto> menus) {
        this.id = id;
        this.storeName = storeName;
        this.categoryName = categoryName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.openTime = openTime;
        this.closedTime = closedTime;
        this.minOrderPrice = minOrderPrice;
        this.status = status;
        this.menus = menus;
    }
}
