package com.api.yirang.auth.application.advancedService;

import com.api.yirang.auth.application.basicService.KakaoTokenService;
import com.api.yirang.auth.application.basicService.UserService;
import com.api.yirang.auth.domain.jwt.JwtParser;
import com.api.yirang.auth.domain.jwt.JwtProvider;
import com.api.yirang.auth.domain.jwt.JwtValidator;
import com.api.yirang.auth.domain.kakaoToken.dto.KakaoUserInfo;
import com.api.yirang.auth.domain.kakaoToken.model.KakaoToken;
import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.auth.presentation.VO.SignInResponseVO;
import com.api.yirang.auth.presentation.dto.SignInRequestDto;
import com.api.yirang.auth.support.type.Authority;
import com.api.yirang.common.support.time.MyLocalTime;
import com.api.yirang.common.support.time.TimeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    // Services DI
    private final KakaoTokenService kakaoTokenService;
    private final UserService userService;

    // JWT DI
    private final JwtProvider jwtProvider;
    private final JwtParser jwtParser;
    private final JwtValidator jwtValidator;

    public SignInResponseVO signin(SignInRequestDto signInRequestDto) {
        String kakaoAccessToken = signInRequestDto.getAccessToken();
        String kakaoRefreshToken = signInRequestDto.getRefreshToken();
        String kakaoExpiredTime = MyLocalTime.makeExpiredTimeString(signInRequestDto.getRefreshTokenExpiredTime());

        // kakaoAccessToken의 유효성 검사
        kakaoTokenService.isValidAccessToken(kakaoAccessToken);

        // SignInRequest의 아이디 얻기
        Long userId = kakaoTokenService.getUserIdByToken(kakaoAccessToken);

        // kakaoToken 정보 저장하기
        KakaoToken kakaoToken = KakaoToken.builder()
                                          .kakaoAccessToken(kakaoAccessToken)
                                          .kakaoRefreshToken(kakaoRefreshToken)
                                          .kakaoRefreshExpiredTime(
                                                  TimeConverter.StringToLocalDateTime(kakaoExpiredTime))
                                          .build();
        kakaoTokenService.saveKakaoToken(kakaoToken);

        // 이전에 등록한 User인지 확인
        if (!userService.isRegisteredUserByUserId(userId)){
            // 처음 등록한 사람이면 kakaoUserInfo 얻어오기
            KakaoUserInfo kakaoUserInfo = kakaoTokenService.getUserInfoByToken(kakaoAccessToken);
            // User 정보 저장하기
            User user = User.builder()
                            .userId(userId)
                            .fileUrl(kakaoUserInfo.getUsername())
                            .sex(kakaoUserInfo.getSex())
                            .email(kakaoUserInfo.getEmail())
                            .authority(Authority.ROLE_USER)
                            .build();
            userService.saveUser(user);
        }
        // User의 authority 판단 하기
        Authority authority = userService.getAuthorityByUserId(userId);

        // 얻은 정보로 YAT(yirang access Token 생성)
        // TO-DO

        return null;
    }
}
