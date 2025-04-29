package org.example.baedalteam27.domain.store.dto.response;

import lombok.Getter;

@Getter
public class StoreNameResponseDto {
    private final Long id;
    private final String storeName;

    public StoreNameResponseDto(Long id, String storeName) {
        this.id = id;
        this.storeName = storeName;
    }
}
