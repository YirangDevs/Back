package com.api.yirang.img.application;


import com.api.yirang.auth.application.intermediateService.UserService;
import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.img.component.S3Uploader;
import com.api.yirang.img.dto.ImgTypeRequestDto;
import com.api.yirang.img.exception.ImageNullException;
import com.api.yirang.img.exception.ImgTypeDuplicatedException;
import com.api.yirang.img.model.Img;
import com.api.yirang.img.repository.ImgRepository;
import com.api.yirang.img.util.ImgType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class ImgService {


    // DI services
    private final UserService userService;

    // DI component
    private final S3Uploader s3Uploader;

    // DI Dao
    private final ImgRepository imgRepository;

    public void updateKaKaoImg(Long userId, String fileUrl) {

        User user = userService.findUserByUserId(userId);

        // FileURL이 Null 일 때는 이전 값 유지
        if (fileUrl == null){
            return;
        }
        imgRepository.updateKakaoImg(userId, fileUrl);
    }

    public void updateCustomImg(Long userId, MultipartFile file){
        User user = userService.findUserByUserId(userId);
        if (file == null){
            return;
        }

        // 1. File Upload
        String fileUrl = s3Uploader.upload(file);
        imgRepository.updateCustomImg(userId, fileUrl);
    }


    public void updateImgType(Long userId, ImgTypeRequestDto imgTypeRequestDto) {
        ImgType newImgType = imgTypeRequestDto.getImgType();
        if (getMyImgType(userId).equals(newImgType)){
            throw new ImgTypeDuplicatedException();
        }
        imgRepository.updateImgType(userId, newImgType);
    }

    public ImgType getMyImgType(Long userId) {
        User user = userService.findUserByUserId(userId);
        Img img = imgRepository.findImgByUser_UserId(userId).orElseThrow(ImageNullException::new);
        return img.getImgType();
    }

    public String getMyImg(Long userId) {
        User user = userService.findUserByUserId(userId);
        Img img = imgRepository.findImgByUser_UserId(userId).orElseThrow(ImageNullException::new);

        return img.getImgType().equals(ImgType.IMG_TYPE_CUSTOM) ? img.getCustomImgUrl() : img.getKakaoImgUrl();
    }

}
