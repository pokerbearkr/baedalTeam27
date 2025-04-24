package org.example.baedalteam27.domain.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuDto {
    private String name;
    private int price;
    private String description;
    private boolean isSoldOut;
}
