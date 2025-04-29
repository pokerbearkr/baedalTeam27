package org.example.baedalteam27.domain.category.dto.request;

import lombok.Getter;

@Getter
public class CategoryRequestDto {
    private final String name;

    public CategoryRequestDto(String name) {
        this.name = name;
    }
}
