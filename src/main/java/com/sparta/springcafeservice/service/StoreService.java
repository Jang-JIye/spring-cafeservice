package com.sparta.springcafeservice.service;


import com.sparta.springcafeservice.dto.StoreAllResponseDto;
import com.sparta.springcafeservice.dto.StoreRequestDto;
import com.sparta.springcafeservice.dto.StoreResponseDto;
import com.sparta.springcafeservice.entity.Store;
import com.sparta.springcafeservice.entity.User;
import com.sparta.springcafeservice.exception.RestApiException;
import com.sparta.springcafeservice.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Create
    public StoreAllResponseDto createStore(StoreRequestDto requestDto, User user) {
        Store store = storeRepository.save(new Store(requestDto, user));
        //registNum 값이 들어있는 경우 처리 필요! ->registNum 값 정책이 바뀔 시 수정 필요!!
        if (user.getRegistNum() == 0) {
            throw new IllegalArgumentException("사업자가 아닙니다.");
        }
        //Entity -> ResponseDto
        return new StoreAllResponseDto(store);
    }


    //ReadAll
    public List<StoreAllResponseDto> getAllStores() {
        List<Store> storeList = storeRepository.findAll();
        return storeList.stream().map(StoreAllResponseDto::new).collect(Collectors.toList());
    }


    // Read
    public StoreResponseDto getStore(Long id, User user) {

        return new StoreResponseDto(findStore(id));
    }


    // Update
    @Transactional
    public ResponseEntity<String> updateStore(Long id, StoreRequestDto requestDto, User user) {
        Store store = findStore(id);

        // 비밀 번호가 다를 시 예외처리
        if (!passwordEncoder.matches(requestDto.getPassword(), store.getUser().getPassword())) {
            throw new IllegalArgumentException("비밀번호가 다릅니다");
        }
        //가게 사장만 수정 가능
        if (!user.getId().equals(store.getUser().getId())) {
            throw new IllegalArgumentException("사용자가 다릅니다");
        }

        store.update(requestDto);
        // 수정 성공
        return ResponseEntity.ok("수정 성공!");

    }


    // Delete
    @Transactional
    public ResponseEntity<String> deleteStore(Long id, StoreRequestDto requestDto, User user) {
        Store store = findStore(id);

        // 비밀 번호가 다를 시 예외처리
        if (!passwordEncoder.matches(requestDto.getPassword(), store.getUser().getPassword())) {
            throw new IllegalArgumentException("비밀번호가 다릅니다");
        }
        //가게 사장만 삭제 가능
        if (!user.getId().equals(store.getUser().getId())) {
            throw new IllegalArgumentException("사용자가 다릅니다");
        }

        storeRepository.delete(store);

        return ResponseEntity.ok("삭제 성공!");
    }


    private Store findStore(Long id) {
        // 가게 ID를 사용하여 특정 가게을 조회하고, 존재하지 않을 경우 예외 발생
        return storeRepository.findById(id).orElseThrow(() ->
                new RestApiException("선택한 가게는 존재하지 않습니다.", HttpStatus.NOT_FOUND.value())
        );
    }
}
