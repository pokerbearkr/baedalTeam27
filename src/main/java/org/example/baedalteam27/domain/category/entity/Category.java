package org.example.baedalteam27.domain.category.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "category")
@Getter
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    public Category (String name) {
        this.name = name;
    }

    // 카테고리 수정
    public void update(String name) {
        this.name = name;
    };
}
