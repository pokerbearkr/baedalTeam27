package org.example.baedalteam27.domain.menu.controller;

import lombok.RequiredArgsConstructor;
import org.example.baedalteam27.domain.menu.dto.MenuDto;
import org.example.baedalteam27.domain.menu.entity.Menu;
import org.example.baedalteam27.domain.menu.service.MenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<Menu> createMenu(@RequestBody MenuDto menuDto) {
        Menu menu = menuService.createMenu(menuDto);
        return ResponseEntity.ok(menu);
    }
    @PutMapping("/{menuId}")
    public ResponseEntity<Menu> updateMenu(@PathVariable Long menuId, @RequestBody MenuDto menuDto) {
        return ResponseEntity.ok(menuService.updateMenu(menuId, menuDto));
    }

    @DeleteMapping("/{menuId}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long menuId) {
        menuService.deleteMenu(menuId);
        return ResponseEntity.noContent().build();
    }
}
