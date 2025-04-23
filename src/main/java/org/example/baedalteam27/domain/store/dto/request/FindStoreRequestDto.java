package org.example.baedalteam27.domain.store.dto.request;

import lombok.Getter;

@Getter
public class FindStoreRequestDto {
    private final String storeName;

    public FindStoreRequestDto(String storeName) {
        this.storeName = storeName;
    }
}
