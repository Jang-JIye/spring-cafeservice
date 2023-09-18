package com.sparta.springcafeservice.repository;

import com.sparta.springcafeservice.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    //특정 가게의 리뷰 모두 조회
    List<Review> findByStoreId(Long storeId);

}