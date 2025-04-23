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
    Page<Store> findStoresByStoreNameContainingAndIsDeletedFalse(String storeName, Pageable pageable);

    // 가게가 폐업 상태가 아닌 가게 조회
    // (N+1문제 @EntityGraph 사용할 예정)
    Optional<Store> findStoreByStoreNameAndIsDeletedFalse(String storeName);

    default Store findStoreByStoreNameAndIsDeletedFalseOrElseThrow(String storeName) {
        return findStoreByStoreNameAndIsDeletedFalse(storeName).orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));
    }

    int countByUserId(Long userId);
}
