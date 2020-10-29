package com.api.yirang.auth.application.advancedService;

import com.api.yirang.auth.application.basicService.KakaoTokenService;
import com.api.yirang.auth.application.intermediateService.UserService;
import com.api.yirang.auth.domain.jwt.components.JwtParser;
import com.api.yirang.auth.domain.jwt.components.JwtProvider;
import com.api.yirang.auth.domain.jwt.components.JwtValidator;
import com.api.yirang.auth.domain.kakaoToken.dto.KakaoUserInfo;
import com.api.yirang.auth.domain.user.converter.UserConverter;
import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.auth.presentation.VO.RefreshResponseVO;
import com.api.yirang.auth.presentation.VO.SignInResponseVO;
import com.api.yirang.auth.presentation.dto.SignInRequestDto;
import com.api.yirang.auth.support.type.Authority;
import com.api.yirang.auth.support.utils.ParsingHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    // Services DI
    private final KakaoTokenService kakaoTokenService;
    private final UserService userService;

    // JWT DI
    private final JwtProvider jwtProvider;
    private final JwtParser jwtParser;

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
        String imageUrl = kakaoUserInfo.getFileUrl();

        // 이전에 등록한 User인지 확인
        if (!userService.isRegisteredUserByUserId(userId)){
            System.out.println("처음 등록한 봉사자입니다.");

            User user = UserConverter.fromKakaoUserInfo(userId, kakaoUserInfo, Authority.ROLE_VOLUNTEER);
            // for debugging
            System.out.println("봉사자: " + user);
            System.out.println("봉사자 정보 저장합니다.");
            userService.saveUser(user);
        }
        else{
            System.out.println("이전에 등록했던 봉사자입니다.");
        }

        System.out.println("authority 판단합니다.");

        Authority authority = userService.getAuthorityByUserId(userId);

        System.out.println("봉사자의 authority는: " + authority);

        String yat = jwtProvider.generateJwtToken(username, imageUrl, userId, authority);

        System.out.println("Yat가 성공적으로 만들어졌습니다!" );
        System.out.println("Yat를 보내겠습니다." );

        return SignInResponseVO.builder()
                               .yirangAccessToken(yat)
                               .build();
    }

    @Transactional
    public RefreshResponseVO refresh(String header){
        String YAT = ParsingHelper.parseHeader(header);

        System.out.println("[AuthService]: YAT를 받았습니다.: " + YAT);

        String username = jwtParser.getUsernameFromJwt(YAT);
        String imageUrl = jwtParser.getImageUrlFromJwt(YAT);
        Long userId = jwtParser.getUserIdFromJwt(YAT);
        Authority authority = jwtParser.getRoleFromJwt(YAT);

        String newYAT = jwtProvider.generateJwtToken(username, imageUrl, userId, authority);

        System.out.println("[AuthService]: 새로운 YAT를 보내겠습니다.: " + newYAT);

        return RefreshResponseVO.builder()
                                .yirangAccessToken(newYAT)
                                .build();

    }
    @Transactional
    public RefreshResponseVO refreshAuthority(String header, Authority authority){
        String YAT = ParsingHelper.parseHeader(header);

        String username = jwtParser.getUsernameFromJwt(YAT);
        String imageUrl = jwtParser.getImageUrlFromJwt(YAT);
        Long userId = jwtParser.getUserIdFromJwt(YAT);

        System.out.println("[AuthService]: 새로운 Authority 입니다: " + authority);

        String newYAT = jwtProvider.generateJwtToken(username, imageUrl, userId, authority);
        return RefreshResponseVO.builder()
                                .yirangAccessToken(newYAT)
                                .build();
    }

    @Transactional
    public RefreshResponseVO refreshToAdmin(String header){
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
        userService.registerAdmin(userId);
        return refreshAuthority(header, Authority.ROLE_ADMIN);
    }

    @Transactional
    public RefreshResponseVO refreshFromAdmin(String header){
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
        userService.fireAdmin(userId);
        return refreshAuthority(header, Authority.ROLE_VOLUNTEER);
    }

}
