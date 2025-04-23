package org.example.baedalteam27.domain.category.dto;

import lombok.Getter;

@Getter
public class UpdateCategoryRequestDto {
    private final String name;

    public UpdateCategoryRequestDto(String name) {
        this.name = name;
    }
}
