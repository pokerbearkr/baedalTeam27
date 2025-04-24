package org.example.baedalteam27.domain.category.dto.response;

import lombok.Getter;

@Getter
public class FindCategoriesResponseDto {
    private final Long id;
    private final String name;

    public FindCategoriesResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
