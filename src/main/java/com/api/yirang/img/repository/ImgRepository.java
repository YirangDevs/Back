package com.api.yirang.img.repository;

import com.api.yirang.img.model.Img;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ImgRepository extends JpaRepository<Img, Long> {

    Optional<Img> findImgByUser_UserId(Long userId);

    @Modifying
    @Transactional
    void deleteImgByUser_UserId(Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE Img I " +
           "SET I.kakaoImgUrl =:fileUrl " +
           "WHERE I.user.userId =:userId")
    void updateKakaoImg(Long userId, String fileUrl);

    @Modifying
    @Transactional
    @Query("UPDATE Img I " +
           "SET I.customImgUrl =:fileUrl " +
           "WHERE I.user.userId =:userId")
    void updateCustomImg(Long userId, String fileUrl);
}
