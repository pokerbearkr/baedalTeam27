package org.example.baedalteam27.domain.category.dto;

import lombok.Getter;

@Getter
public class CategoryResponseDto {
    private final Long id;
    private final String name;

    public CategoryResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
