package org.example.baedalteam27.domain.store.repository;

import org.example.baedalteam27.domain.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    // 가게가 폐업 상태가 아닌 가게만 조회, storeName 단어가 들어간 가게들 조회
    Page<Store> findByStoreNameContainingAndIsDeletedFalse(String storeName, Pageable pageable);

    Optional<Store> findByIdAndIsDeletedFalse(Long storeId);

    // 카테고리Id로 가게 조회
    Page<Store> findByCategoryIdAndIsDeletedFalse(Long categoryId, Pageable pageable);

    // 가게가 폐업 상태가 아닌 가게 조회
    // (N+1문제 @EntityGraph 사용할 예정)
    Optional<Store> findByCategoryIdAndIdAndIsDeletedFalse(Long categoryId, Long storeId);

    int countByUserId(Long userId);
}
