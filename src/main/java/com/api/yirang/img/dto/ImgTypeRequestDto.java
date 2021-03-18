package com.api.yirang.img.dto;


import com.api.yirang.img.util.ImgType;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ImgTypeRequestDto {

    private final ImgType imgType;

    public ImgTypeRequestDto() {
        this.imgType = null;
    }
}
