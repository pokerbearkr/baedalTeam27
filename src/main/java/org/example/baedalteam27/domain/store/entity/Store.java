package org.example.baedalteam27.domain.store.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.baedalteam27.domain.category.entity.Category;
import org.example.baedalteam27.domain.menu.entity.Menu;
import org.example.baedalteam27.domain.store.enums.Status;
import org.example.baedalteam27.domain.user.entitiy.User;
import org.hibernate.annotations.SQLDelete;


import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "store")
@NoArgsConstructor
@SQLDelete(sql = "UPDATE store SET is_deleted = true WHERE id = ?")  // delete() 는 기본적으로 엔티티의 PK를 기준으로 삭제
public class Store {

    // 기본키
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 가게명
    @Column(nullable = false, unique = true)
    private String storeName;

    // 주소
    @Column(nullable = false)
    private String address;

    // 전화번호
    @Column(nullable = false)
    private String phoneNumber;

    // 오픈시간
    @Column(nullable = false)
    private LocalTime openTime;

    // 마감시간
    @Column(nullable = false)
    private LocalTime closedTime;

    // 최소 주문금액
    @Column(nullable = false)
    private Long minOrderPrice;

    // 상태
    @Column(name = "is_deleted")
    private boolean isDeleted;

    // 유저
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // 카테고리
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // 메뉴
    @OneToMany(mappedBy = "store")
    private List<Menu> menus = new ArrayList<>();  // menus null 값 방지 초기화

    // 파라미터가 달라지는 여러 생성자를 위해 빌더 적용
    @Builder
    public Store (String storeName,
                  String address,
                  String phoneNumber,
                  LocalTime openTime,
                  LocalTime closedTime,
                  Long minOrderPrice,
                  User user,
                  Category category
                  ) {
        this.storeName = storeName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.openTime = openTime;
        this.closedTime = closedTime;
        this.minOrderPrice = minOrderPrice;
        this.user =user;
        this.category = category;
    }

    // 가게 수정
    public void update(String storeName,
                       String address,
                       String phoneNumber,
                       LocalTime openTime,
                       LocalTime closedTime,
                       Long minOrderPrice,
                       Category category
    ) {
        this.storeName = storeName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.openTime = openTime;
        this.closedTime = closedTime;
        this.minOrderPrice = minOrderPrice;
        this.category = category;
    }

    // 가게 운영 상태
    public Status getCurrentStatus(LocalTime now) {
        if (now.isAfter(openTime) && now.isBefore(closedTime)) {
            return Status.OPEN;
        } else {
            return Status.CLOSED;
        }
    }
}
