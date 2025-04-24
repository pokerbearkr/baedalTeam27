package org.example.baedalteam27.domain.order.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.baedalteam27.domain.user.entitiy.User;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private LocalDateTime orderedTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    //Order_id 뺀 생성자
    public Order(User user, Store store, String location,
          LocalDateTime orderedTime, OrderStatus orderStatus){
        this.user = user;
        this.store = store;
        this.location = location;
        this.orderedTime = orderedTime;
        this.orderStatus = orderStatus;
    }
}