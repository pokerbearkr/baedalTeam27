package org.example.baedalteam27.domain.store.controller;

import lombok.RequiredArgsConstructor;

import org.example.baedalteam27.domain.store.dto.request.SaveStoreRequestDto;
import org.example.baedalteam27.domain.store.dto.request.UpdateStoreRequestDto;
import org.example.baedalteam27.domain.store.dto.response.SaveStoreResponseDto;
import org.example.baedalteam27.domain.store.dto.response.StoreNameResponseDto;
import org.example.baedalteam27.domain.store.dto.response.StoreResponseDto;
import org.example.baedalteam27.domain.store.service.StoreService;
import org.example.baedalteam27.global.jwt.LoginUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    // 가게 등록
    @PostMapping("api/stores")
    public ResponseEntity<SaveStoreResponseDto> saveStore (
            @LoginUser Long userId,
            @RequestBody SaveStoreRequestDto requestDto
    ) {
        SaveStoreResponseDto saveStoreResponseDto = storeService.saveStore(userId, requestDto);
        return ResponseEntity.ok(saveStoreResponseDto);
    }

    // 가게 전체 리스트 조회 (카테고리에 해당하는 가게명 또는 입력받은 단어가 들어가는 가게명)
    @GetMapping("api/stores")
    public ResponseEntity<Page<StoreNameResponseDto>> findStores (
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String storeName,
            @PageableDefault(direction = Sort.Direction.ASC, sort = "storeName") Pageable pageable
    ) {
        Page<StoreNameResponseDto> storeNameResponseDto = storeService.findStores(categoryId, storeName, pageable);
        return ResponseEntity.ok(storeNameResponseDto);
    }

    // 가게 단건 조회
    @GetMapping("api/stores/{storeid}")
    public ResponseEntity<StoreResponseDto> findStore (
            @PathVariable Long storeid
    ) {
        StoreResponseDto store = storeService.findStore(storeid);
        return ResponseEntity.ok(store);
    }

    // 가게 수정
    @PatchMapping("api/stores/{storeid}")
    public ResponseEntity<Void> updateStore (
            @LoginUser Long userId,
            @RequestBody UpdateStoreRequestDto requestDto,
            @PathVariable Long storeid
    ) {
        storeService.updateStore(userId, requestDto, storeid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 가게 폐업
    @DeleteMapping("api/stores/{storeid}")
    public ResponseEntity<Void> deleteStore (
            @LoginUser Long userId,
            @PathVariable Long storeid
    ) {
        storeService.deleteStore(userId, storeid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
