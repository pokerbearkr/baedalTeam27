package org.example.baedalteam27.domain.store.service;

import lombok.RequiredArgsConstructor;
import org.example.baedalteam27.domain.category.entity.Category;
import org.example.baedalteam27.domain.category.repository.CategoryRepository;
import org.example.baedalteam27.domain.store.dto.request.SaveStoreRequestDto;
import org.example.baedalteam27.domain.store.dto.request.UpdateStoreRequestDto;
import org.example.baedalteam27.domain.store.dto.response.SaveStoreResponseDto;
import org.example.baedalteam27.domain.store.dto.response.StoreNameResponseDto;
import org.example.baedalteam27.domain.store.dto.response.StoreResponseDto;
import org.example.baedalteam27.domain.store.entity.Store;
import org.example.baedalteam27.domain.store.repository.StoreRepository;
import org.example.baedalteam27.domain.user.UserRole;
import org.example.baedalteam27.domain.user.entitiy.User;
import org.example.baedalteam27.domain.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final CategoryRepository categoryRepository;


    // 가게 등록
    public SaveStoreResponseDto saveStore(User loginUser, SaveStoreRequestDto requestDto, Long categoryId) {
        // 유저, 카테고리 조회
        User user = userRepository.findById(loginUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리가 존재하지 않습니다."));

        // 유저가 사장님 권한을 가졌는지 검증
        if (!loginUser.getRole().equals(UserRole.OWNER)) {
            throw new IllegalArgumentException("가게를 등록할 권한이 없습니다.");
        }

        // 소요한 가게가 3개 이하인지 검증
        int storeCount = storeRepository.countByUserId(loginUser.getId());
        if (storeCount >= 4) {
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
                saveStore.getAddress(),
                saveStore.getPhoneNumber(),
                saveStore.getOpenTime(),
                saveStore.getClosedTime(),
                saveStore.getMinOrderPrice()
        );
    }

    // 가게 전체 리스트 조회 (카테고리에 해당하는 가게명 또는 입력받은 단어가 들어가는 가게명)
    public Page<StoreNameResponseDto> findStores(Long categoryId, String storeName, Pageable pageable) {
        // id 와 name 둘 다 기입하거나 둘 다 기입하지 않았을 때 예외처리
        if ((categoryId != null && storeName != null) || (categoryId == null && storeName == null)) {
            throw new IllegalArgumentException("카테고리 또는 가게명 중 하나만 입력!");
        }

        // 입력받은 단어가 들어가는 가게명
        if (storeName != null) {
            return storeRepository.findByStoreNameContainingAndIsDeletedFalse(storeName, pageable)
                    .map(store -> new StoreNameResponseDto(store.getId(), store.getStoreName()));
        }

        // 카테고리에 해당하는 가게명
        return storeRepository.findByCategoryIdAndIsDeletedFalse(categoryId, pageable)
                .map(store -> new StoreNameResponseDto(store.getId(), store.getStoreName()));
    }

    // 가게 단건 조회(카테고리의 기게들 중 하나의 가게 조회, 전체 가게들 중 하나의 가게 조회)
    public StoreResponseDto findStore(Long categoryId, Long storeId) {
        Store store; // 변수 선언
        // 필수값 검증
        if (storeId == null) {
            throw new IllegalArgumentException("가게 번호는 필수!");
        }

        // 카테고리에 해당하는 가게 찾기
        if (categoryId != null) {
            store = storeRepository.findByCategoryIdAndIdAndIsDeletedFalse(categoryId, storeId)
                    .orElseThrow(() -> new IllegalArgumentException("카테고리에 해당된 가게를 찾을 수 없습니다."));
        } else {
            // 전체에서 가게 조회
            store = storeRepository.findByIdAndIsDeletedFalse(storeId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 이름의 가게를 찾을 수 없습니다."));
        }

        // 메뉴를 MenuResponseDto에 담고 리스트로 변환 (N+1 문제 해결 예정)
        List<MenuResponseDto> menuResponseDtoList = store.getMenus().stream()
                .map(menu -> new MenuResponseDto(menu.getName(), menu.getPrice(), menu.getDescription()))
                .collect(Collectors.toList());

        return new StoreResponseDto(
                store.getId(),
                store.getStoreName(),
                store.getAddress(),
                store.getPhoneNumber(),
                store.getOpenTime(),
                store.getClosedTime(),
                store.getMinOrderPrice(),
                menuResponseDtoList);
    }

    // 가게 수정
    public void updateStore(Long userId, UpdateStoreRequestDto requestDto, Long storeId) {

        // 가게 찾기
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게가 존재하지 않습니다."));

        // 유저가 가게를 등록한 유저인지 검증
        if (!userId.equals(store.getUser().getId())) {
            throw new IllegalArgumentException("해당 가게에 대한 수정 권한이 없습니다.");
        }

        // 가게 수정된 정보 저장
        store.update(
                requestDto.getStoreName(),
                requestDto.getAddress(),
                requestDto.getPhoneNumber(),
                requestDto.getOpenTime(),
                requestDto.getClosedTime(),
                requestDto.getMinOrderPrice()
        );
    }

    // 가게 폐업
    public void deleteStore(User loginUser, Long storeId) {

        // 유저가 사장님 권한을 가졌는지 검증
        if (!loginUser.getRole().equals(UserRole.OWNER)) {
            throw new IllegalArgumentException("가게 사장님이 아닙니다.");
        }

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게가 존재하지 않습니다."));

        // 유저가 가게를 등록한 유저인지 검증
        if (!loginUser.getId().equals(store.getUser().getId())) {
            throw new IllegalArgumentException("해당 가게에 대한 삭제 권한이 없습니다.");
        }

        storeRepository.delete(store);
    }
}