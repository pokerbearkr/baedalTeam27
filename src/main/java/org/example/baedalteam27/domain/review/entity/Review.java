package org.example.baedalteam27.domain.review.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.baedalteam27.domain.order.entity.Order;
import org.example.baedalteam27.domain.store.entity.Store;
import org.example.baedalteam27.domain.user.entitiy.User;
import org.example.baedalteam27.global.entity.BaseEntity;


@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "review")
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    float starScore;

    @Column(nullable = false)
    String reviewText;

    String ImgURL;

    @ManyToOne
    @JoinColumn
    Store store;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @OneToOne
    @JoinColumn(name = "orders")
    Order order;

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
}
