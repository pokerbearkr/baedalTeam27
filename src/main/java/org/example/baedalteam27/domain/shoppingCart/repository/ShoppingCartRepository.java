package org.example.baedalteam27.domain.shoppingCart.repository;

import org.example.baedalteam27.domain.shoppingCart.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart,Long> {
    // 장바구니와 담긴 메뉴 가져오기
    @Query("SELECT c FROM ShoppingCart c " +
            "JOIN FETCH c.menu m " +
            "JOIN FETCH c.store s " +
            "WHERE c.user.id = :userId")
    List<ShoppingCart> findByUserIdWithStoreAndMenu(@Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM ShoppingCart c WHERE c.user.id = :userId AND c.menu.id = :menuId")
    void deleteByUserIdAndMenuId(@Param("userId") Long userId, @Param("menuId") Long menuId);

    @Modifying
    @Query("DELETE FROM ShoppingCart c WHERE c.user.id = :userId")
    void deleteAllByUserId(@Param("userId")Long userId);
}
