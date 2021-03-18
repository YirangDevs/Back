package com.api.yirang.img.controller;

import com.api.yirang.auth.application.intermediateService.UserService;
import com.api.yirang.auth.domain.jwt.components.JwtParser;
import com.api.yirang.auth.support.utils.ParsingHelper;
import com.api.yirang.img.application.ImgService;
import com.api.yirang.img.dto.ImgTypeRequestDto;
import com.api.yirang.img.util.ImgType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/apis/imgs")
public class ImgController {

    // DI services
    private final ImgService imgService;

    // DI Jwt
    private final JwtParser jwtParser;

    /**
     * 목적: 자신의 ImgType을 알 수 있는 API
     * 사용자: 슈퍼 관리자, 관리자, 봉사자
     */
    @GetMapping(value = "/imgTypes", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, ImgType> getMyImgType(@RequestHeader("Authorization") String header){
        System.out.println("[ImgController] 자신의 이미지 타입 GET을 시도합니다.");

        Map<String, ImgType> res = new HashMap<>();
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
        res.put("imgType", imgService.getMyImgType(userId));
        return res;
    }

    /**
     * 목적: 자신의 ImgUrl을 알 수 있는 API
     * 사용자: 슈퍼 관리자, 관리자, 봉사자
     */
    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> getMyImg(@RequestHeader("Authorization") String header){
        System.out.println("[ImgController] 자신의 이미지를 GET을 시도합니다.");

        Map<String, String> res = new HashMap<>();
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
        res.put("imgUrl", imgService.getMyImg(userId));
        return res;
    }


    /**
     * 목적: 자신의 customImg를 업로드 할 수 있는 API
     * 사용자: 슈퍼 관리자, 관리자, 봉사자
     */
    @PostMapping(value = "/custom", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void postCustomImg(@RequestHeader("Authorization") String header,
                              @RequestParam("customImg")MultipartFile file){
        System.out.println("[ImgController] Custom Img 업로드를 시도합니다.");
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
        imgService.updateCustomImg(userId, file);
    }

    /**
     * 목적: imgType을 변경할 수 있는 API
     * 사용자: 슈퍼 관리자, 관리자, 봉사자
     */
    @PutMapping(value = "/imgTypes", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void changeImgType(@RequestHeader("Authorization") String header,
                              @RequestBody ImgTypeRequestDto imgTypeRequestDto){
        System.out.println("[ImgController] ImgType Change를 시도합니다.");
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
        imgService.updateImgType(userId, imgTypeRequestDto);
    }


}
