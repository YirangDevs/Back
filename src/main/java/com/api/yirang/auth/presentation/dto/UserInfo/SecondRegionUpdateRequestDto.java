package com.api.yirang.auth.presentation.dto.UserInfo;


import com.api.yirang.common.support.type.Region;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SecondRegionUpdateRequestDto {

    private final Region secondRegion;

    public SecondRegionUpdateRequestDto() {
        this.secondRegion = null;
    }
}
