package org.example.baedalteam27.domain.store.dto.request;

import lombok.Getter;

import java.time.LocalTime;

@Getter
public class SaveStoreRequestDto {
    private final String storeName;
    private final String address;
    private final String phoneNumber;
    private final LocalTime openTime;
    private final LocalTime closedTime;
    private final Long minOrderPrice;
    private final Long categoryId;

    public SaveStoreRequestDto(String storeName, String address, String phoneNumber, LocalTime openTime, LocalTime closedTime, Long minOrderPrice, Long categoryId) {
        this.storeName = storeName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.openTime = openTime;
        this.closedTime = closedTime;
        this.minOrderPrice = minOrderPrice;
        this.categoryId = categoryId;
    }
}
