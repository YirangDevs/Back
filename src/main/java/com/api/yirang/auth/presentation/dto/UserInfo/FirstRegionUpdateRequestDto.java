package com.api.yirang.auth.presentation.dto.UserInfo;


import com.api.yirang.common.support.type.Region;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class FirstRegionUpdateRequestDto {

    private final Region firstRegion;

    public FirstRegionUpdateRequestDto() {
        this.firstRegion = null;
    }
}
