package org.example.baedalteam27.domain.menu.repository;

import org.example.baedalteam27.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByStoreId(Long storeId);
}
