package com.api.yirang.img.repository;

import com.api.yirang.img.model.Img;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ImgRepository extends JpaRepository<Img, Long> {

    @Modifying
    @Transactional
    void deleteImgByUser_UserId(Long userId);

}
