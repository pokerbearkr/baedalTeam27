package org.example.baedalteam27.domain.store.service;

import lombok.RequiredArgsConstructor;
import org.example.baedalteam27.domain.store.dto.request.FindStoreRequestDto;
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
public class StoreService{

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final CategoryRepository categoryRepository; // 카테고리 만들면 사용 할 예정



    // 가게 등록
    public SaveStoreResponseDto saveStore(User loginUser, SaveStoreRequestDto requestDto) {
        // 유저, 카테고리, 메뉴 id 조회
        User user = userRepository.findById(loginUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

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

    // 가게 전체 리스트 조회(가게명만 출력)
    public Page<StoreNameResponseDto> findStores (FindStoreRequestDto requestDto, Pageable pageable) {
        Page<Store> storeByStoreNameContaining = storeRepository.findStoresByStoreNameContainingAndIsDeletedFalse(
                requestDto.getStoreName(), pageable);

        return storeByStoreNameContaining.map(store -> new StoreNameResponseDto(store.getStoreName()));
    }

    // 가게 단건 조회
    public StoreResponseDto findStore(String storeName) {

        // 가게 찾기
        Store store = storeRepository.findStoreByStoreNameAndIsDeletedFalseOrElseThrow(storeName);

        // 메뉴를 MenuResponseDto에 담고 리스트로 변환 (N+1 문제 해결 예정)
        List<MenuResponseDto> menuResponseDtoList = store.getMenus().stream()
                .map(menu -> new MenuResponseDto(menu.getName(), menu.getPrice(), menu.getDescription))
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
    public void updateStore(Long userId, String storeName, UpdateStoreRequestDto requestDto) {

        // 가게 찾기
        Store store = storeRepository.findStoreByStoreNameAndIsDeletedFalseOrElseThrow(storeName);

        // 유저 id가 일치하는지 검증
        if (userId.equals(store.getUser().getId())) {
            throw new IllegalArgumentException("유저가 일치 하지 않습니다.");
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
    public void deleteStore(String storeName, User loginUser) {

        // 유저가 사장님 권한을 가졌는지 검증
        if (!loginUser.getRole().equals(UserRole.OWNER)) {
            throw new IllegalArgumentException("가게를 등록할 권한이 없습니다.");
        }

        Store store = storeRepository.findStoreByStoreNameAndIsDeletedFalseOrElseThrow(storeName);

        // 유저가 가게를 등록한 유저인지 검증
        if (loginUser.getId().equals(store.getUser().getId())) {
            throw new IllegalArgumentException("유저가 일치 하지 않습니다.");
        }

        storeRepository.delete(store);
    }
}