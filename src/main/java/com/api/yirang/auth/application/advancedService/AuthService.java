package com.api.yirang.auth.application.advancedService;

import com.api.yirang.auth.application.basicService.KakaoTokenService;
import com.api.yirang.auth.application.intermediateService.UserService;
import com.api.yirang.auth.domain.jwt.components.JwtProvider;
import com.api.yirang.auth.domain.kakaoToken.dto.KakaoUserInfo;
import com.api.yirang.auth.domain.user.converter.UserConverter;
import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.auth.presentation.VO.SignInResponseVO;
import com.api.yirang.auth.presentation.dto.SignInRequestDto;
import com.api.yirang.auth.support.type.Authority;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("AuthService")
@RequiredArgsConstructor
public class AuthService {

    // Services DI
    private final KakaoTokenService kakaoTokenService;
    private final UserService userService;

    // JWT DI
    private final JwtProvider jwtProvider;

    @Transactional
    public SignInResponseVO signin(SignInRequestDto signInRequestDto) {
        String kakaoAccessToken = signInRequestDto.getAccessToken();

        // kakaoAccessToken의 유효성 검사
        kakaoTokenService.isValidAccessToken(kakaoAccessToken);

        // SignInRequest의 아이디 얻기
        Long userId = kakaoTokenService.getUserIdByToken(kakaoAccessToken);

        System.out.println("유저 확인 합니다");

        // KakaoUserInfo 얻기
        KakaoUserInfo kakaoUserInfo = kakaoTokenService.getUserInfoByToken(kakaoAccessToken);

        // userName이랑 imageUrl 얻기
        String username = kakaoUserInfo.getUsername();
        String imageUrl = kakaoUserInfo.getUsername();

        String lastPoop = "";

        // 이전에 등록한 User인지 확인
        if (!userService.isRegisteredUserByUserId(userId)){
            System.out.println("처음 등록한 유저입니다.");

            User user = UserConverter.fromKakaoUserInfo(userId, kakaoUserInfo, Authority.ROLE_USER);
            // for debugging
            System.out.println("유저: " + user);
            System.out.println("유저 정보 저장합니다.");
            userService.saveUser(user);
        }
        else{
            System.out.println("이전에 등록했던 유저입니다.");
        }

        System.out.println("authority 판단합니다.");

        Authority authority = userService.getAuthorityByUserId(userId);

        System.out.println("User의 authority는: " + authority);

        String yat = jwtProvider.generateJwtToken(username, imageUrl, userId, authority);

        System.out.println("Yat가 성공적으로 만들어졌습니다!" );
        System.out.println("Yat를 보내겠습니다." );

        return SignInResponseVO.builder()
                               .yirangAccessToken(yat)
                               .build();
    }

}
