package org.example.baedalteam27.domain.store.service;


import lombok.RequiredArgsConstructor;
import org.example.baedalteam27.domain.category.entity.Category;
import org.example.baedalteam27.domain.category.repository.CategoryRepository;
import org.example.baedalteam27.domain.menu.dto.MenuDto;
import org.example.baedalteam27.domain.store.dto.request.SaveStoreRequestDto;
import org.example.baedalteam27.domain.store.dto.request.UpdateStoreRequestDto;
import org.example.baedalteam27.domain.store.dto.response.SaveStoreResponseDto;
import org.example.baedalteam27.domain.store.dto.response.StoreNameResponseDto;
import org.example.baedalteam27.domain.store.dto.response.StoreResponseDto;
import org.example.baedalteam27.domain.store.entity.Store;
import org.example.baedalteam27.domain.store.enums.Status;
import org.example.baedalteam27.domain.store.repository.StoreRepository;
import org.example.baedalteam27.domain.user.UserRole;
import org.example.baedalteam27.domain.user.entitiy.User;
import org.example.baedalteam27.domain.user.repository.UserRepository;
import org.example.baedalteam27.global.exception.ForbiddenException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;


    // 가게 등록
    @Transactional
    public SaveStoreResponseDto saveStore(Long userId, SaveStoreRequestDto requestDto) {
        // 유저, 카테고리 조회
        User user = userRepository.getUserByUserId(userId);

        Category category = categoryRepository.findByIdAndIsDeletedFalseOrElseThrow(requestDto.getCategoryId());

        // 유저가 사장님 권한을 가졌는지 검증
        if (!user.getRole().equals(UserRole.OWNER)) {
            throw new ForbiddenException("가게를 등록할 권한이 없습니다.");
        }

        // 소요한 가게가 3개 이하인지 검증
        int storeCount = storeRepository.countByUserIdAndIsDeletedFalse(userId);
        if (storeCount >= 3) {
            throw new IllegalArgumentException("가게는 최대 3개까지만 등록할 수 있습니다.");
        }

        Store store = Store.builder()
                .storeName(requestDto.getStoreName())
                .address(requestDto.getAddress())
                .phoneNumber(requestDto.getPhoneNumber())
                .openTime(requestDto.getOpenTime())
                .closedTime(requestDto.getClosedTime())
                .minOrderPrice(requestDto.getMinOrderPrice())
                .user(user)
                .category(category)
                .build();

        // store 객체 저장
        Store saveStore = storeRepository.save(store);

        return new SaveStoreResponseDto(
                saveStore.getId(),
                saveStore.getStoreName(),
                category.getName(),
                saveStore.getAddress(),
                saveStore.getPhoneNumber(),
                saveStore.getOpenTime(),
                saveStore.getClosedTime(),
                saveStore.getMinOrderPrice()
        );
    }

    // 가게 전체 리스트 조회 (카테고리에 해당하는 가게명 또는 입력받은 단어가 들어가는 가게명)
    public Page<StoreNameResponseDto> findStores(Long categoryId, String storeName, Pageable pageable) {
        // 둘 다 입력 했을 때 예외처리
        if (categoryId != null && storeName != null) {
            throw new IllegalArgumentException("카테고리 또는 가게명 중 하나만 입력! 또는 둘 다 입력X");
        }

        // 입력받은 단어가 들어가는 가게명
        if (storeName != null) {
            return storeRepository.findByStoreNameContainingAndIsDeletedFalse(storeName, pageable)
                    .map(store -> new StoreNameResponseDto(store.getId(), store.getStoreName()));
        }

        // 카테고리에 해당하는 가게명
        if (categoryId != null) {
            return storeRepository.findByCategoryIdAndIsDeletedFalse(categoryId, pageable)
                    .map(store -> new StoreNameResponseDto(store.getId(), store.getStoreName()));
        }

        // 둘 다 입력하지 않았을 때 전체 리스트 조회
        return storeRepository.findByIsDeletedFalse(pageable)
                .map(store -> new StoreNameResponseDto(store.getId(), store.getStoreName()));
    }

    // 가게 단건 조회
    public StoreResponseDto findStore(Long storeId) {
        // 필수값 검증
        if (storeId == null) {
            throw new IllegalArgumentException("가게 번호는 필수!");
        }

        // 전체에서 가게 조회
        Store store = storeRepository.findByIdAndIsDeletedFalseOrElseThrow(storeId);

        // 메뉴를 MenuResponseDto에 담고 리스트로 변환 (N+1 문제 해결 예정)
        List<MenuDto> menuDtoList = store.getMenus()
                .stream()
                .map(menu -> new MenuDto(
                        menu.getId(),
                        menu.getName(),
                        menu.getPrice(),
                        menu.getDescription(),
                        menu.isSoldOut()))
                .toList();

        // 가게 상태 조회
        Status currentStatus = store.getCurrentStatus(LocalTime.now());

        return new StoreResponseDto(
                store.getId(),
                store.getStoreName(),
                store.getCategory().getName(),
                store.getAddress(),
                store.getPhoneNumber(),
                store.getOpenTime(),
                store.getClosedTime(),
                store.getMinOrderPrice(),
                currentStatus.toString(),
                menuDtoList);
    }

    // 가게 수정
    @Transactional
    public void updateStore(Long userId, UpdateStoreRequestDto requestDto, Long storeId) {

        // 가게 찾기
        Store store = storeRepository.findByIdAndIsDeletedFalseOrElseThrow(storeId);

        // 유저가 가게를 등록한 유저인지 검증
        if (!userId.equals(store.getUser().getId())) {
            throw new ForbiddenException("해당 가게에 대한 수정 권한이 없습니다.");
        }

        // 카테고리 조회
        Category category = categoryRepository.findByIdAndIsDeletedFalseOrElseThrow(requestDto.getCategoryId());

        // 가게 수정된 정보 저장
        store.update(
                requestDto.getStoreName(),
                requestDto.getAddress(),
                requestDto.getPhoneNumber(),
                requestDto.getOpenTime(),
                requestDto.getClosedTime(),
                requestDto.getMinOrderPrice(),
                category
        );
    }

    // 가게 폐업
    @Transactional
    public void deleteStore(Long userId, Long storeId) {
        User user = userRepository.getUserByUserId(userId);

        // 유저가 사장님 권한을 가졌는지 검증
        if (!user.getRole().equals(UserRole.OWNER)) {
            throw new ForbiddenException("가게 사장님이 아닙니다.");
        }

        Store store = storeRepository.findByIdAndIsDeletedFalseOrElseThrow(storeId);

        // 유저가 가게를 등록한 유저인지 검증
        if (!user.getId().equals(store.getUser().getId())) {
            throw new ForbiddenException("해당 가게에 대한 삭제 권한이 없습니다.");
        }

        // 폐업한 가게를 다시 폐업하려고 할 때
        if (store.isDeleted()) {
            throw new IllegalArgumentException("이미 폐업한 가게 입니다.");
        }

        storeRepository.delete(store);
    }
}