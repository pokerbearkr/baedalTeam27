package org.example.baedalteam27.domain.menu.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.baedalteam27.domain.store.entity.Store;

@Entity
@Table(name = "menu")
@Getter
@NoArgsConstructor
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private boolean isSoldOut;

    @Builder
    public Menu(Store store, String name, int price, String description, boolean isSoldOut) {
        this.store = store;
        this.name = name;
        this.price = price;
        this.description = description;
        this.isSoldOut = isSoldOut;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSoldout(boolean soldout) {
        this.isSoldOut = soldout;
    }

}
