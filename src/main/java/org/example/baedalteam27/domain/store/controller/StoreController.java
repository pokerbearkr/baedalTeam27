package org.example.baedalteam27.domain.store.controller;

import lombok.RequiredArgsConstructor;
import org.example.baedalteam27.domain.store.dto.request.FindStoreRequestDto;
import org.example.baedalteam27.domain.store.dto.request.SaveStoreRequestDto;
import org.example.baedalteam27.domain.store.dto.request.UpdateStoreRequestDto;
import org.example.baedalteam27.domain.store.dto.response.SaveStoreResponseDto;
import org.example.baedalteam27.domain.store.dto.response.StoreNameResponseDto;
import org.example.baedalteam27.domain.store.dto.response.StoreResponseDto;
import org.example.baedalteam27.domain.store.service.StoreService;
import org.example.baedalteam27.domain.user.entitiy.User;
import org.example.baedalteam27.global.jwt.LoginUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    // 가게 등록
    @PostMapping
    public ResponseEntity<SaveStoreResponseDto> saveStore (@LoginUser User loginUser, @RequestBody SaveStoreRequestDto requestDto) {
        SaveStoreResponseDto saveStoreResponseDto = storeService.saveStore(loginUser, requestDto);
        return ResponseEntity.ok(saveStoreResponseDto);
    }

    // 가게 전체 리스트 조회
    @GetMapping
    public ResponseEntity<Page<StoreNameResponseDto>> findStores (
            @RequestBody FindStoreRequestDto requestDto,
            @PageableDefault(direction = Sort.Direction.ASC, sort = "storeName") Pageable pageable
    ) {
        Page<StoreNameResponseDto> storeNameResponseDto = storeService.findStores(requestDto, pageable);
        return ResponseEntity.ok(storeNameResponseDto);
    }

    // 가게 단건 조회
    @GetMapping
    public ResponseEntity<StoreResponseDto> findStore (@RequestParam String storeName) {
        StoreResponseDto store = storeService.findStore(storeName);
        return ResponseEntity.ok(store);
    }

    // 가게 수정
    @PatchMapping
    public ResponseEntity<Void> updateStore (
            @LoginUser Long userId,
            @RequestParam String storeName,
            @RequestBody UpdateStoreRequestDto requestDto
    ) {
        storeService.updateStore(userId, storeName, requestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 가게 폐업
    @DeleteMapping
    public ResponseEntity<Void> deleteStore (@RequestParam String storeName, @LoginUser User loginUser) {
        storeService.deleteStore(storeName, loginUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
