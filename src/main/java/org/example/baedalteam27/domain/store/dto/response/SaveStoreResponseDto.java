package org.example.baedalteam27.domain.store.dto.response;

import lombok.Getter;

import java.time.LocalTime;

@Getter
public class SaveStoreResponseDto {
    private final Long id;
    private final String storeName;
    private final String categoryName;
    private final String address;
    private final String phoneNumber;
    private final LocalTime openTime;
    private final LocalTime closedTime;
    private final Long minOrderPrice;

    public SaveStoreResponseDto(Long id, String storeName, String categoryName, String address, String phoneNumber, LocalTime openTime, LocalTime closedTime, Long minOrderPrice) {
        this.id = id;
        this.storeName = storeName;
        this.categoryName = categoryName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.openTime = openTime;
        this.closedTime = closedTime;
        this.minOrderPrice = minOrderPrice;
    }
}
