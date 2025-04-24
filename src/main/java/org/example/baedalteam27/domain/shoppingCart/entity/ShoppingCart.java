package org.example.baedalteam27.domain.shoppingCart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.baedalteam27.domain.user.entitiy.User;
import org.example.baedalteam27.domain.store.entity.Store;
import org.example.baedalteam27.domain.menu.entity.Menu;

@Entity
@Getter @Setter
@NoArgsConstructor
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Column(nullable = false)
    private int quantity;

    public ShoppingCart(User user, Store store, Menu menu, int quantity){
        this.user = user;
        this.store = store;
        this.menu = menu;
        this.quantity = quantity;
    }
}
