package com.api.yirang.auth.presentation.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FakeSignInRequestDto {


    private final String fakeAuthority;

    public FakeSignInRequestDto() {
        this.fakeAuthority = null;
    }
}
