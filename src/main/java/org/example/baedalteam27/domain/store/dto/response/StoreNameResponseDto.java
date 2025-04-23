package org.example.baedalteam27.domain.store.dto.response;

import lombok.Getter;

@Getter
public class StoreNameResponseDto {
    private final String storeName;

    public StoreNameResponseDto(String storeName) {
        this.storeName = storeName;
    }
}
