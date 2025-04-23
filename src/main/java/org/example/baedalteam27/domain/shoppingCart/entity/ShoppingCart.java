package org.example.baedalteam27.domain.shoppingCart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.baedalteam27.domain.user.entitiy.User;

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

    @Column(nullable = false)
    private Menu menuItem;

    @Column(nullable = false)
    private int quantity;
}
