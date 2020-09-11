package com.api.yirang.auth.application.advancedService;

import com.api.yirang.auth.application.basicService.KakaoTokenService;
import com.api.yirang.auth.application.basicService.UserService;
import com.api.yirang.auth.domain.jwt.JwtParser;
import com.api.yirang.auth.domain.jwt.JwtProvider;
import com.api.yirang.auth.domain.jwt.JwtValidator;
import com.api.yirang.auth.domain.kakaoToken.converter.KakaoTokenConverter;
import com.api.yirang.auth.domain.kakaoToken.dto.KakaoUserInfo;
import com.api.yirang.auth.domain.kakaoToken.exceptions.AlreadyExpiredKakaoRefreshTokenException;
import com.api.yirang.auth.domain.kakaoToken.model.KakaoToken;
import com.api.yirang.auth.domain.user.converter.UserConverter;
import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.auth.presentation.VO.RefreshYatResponseVO;
import com.api.yirang.auth.presentation.VO.SignInResponseVO;
import com.api.yirang.auth.presentation.dto.SignInRequestDto;
import com.api.yirang.auth.support.type.Authority;
import com.api.yirang.auth.support.utils.ParsingHelper;
import com.api.yirang.common.support.time.MyLocalTime;
import com.api.yirang.common.support.time.TimeConverter;
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
    private final JwtParser jwtParser;
    private final JwtProvider jwtProvider;

    @Transactional
    public SignInResponseVO signin(SignInRequestDto signInRequestDto) {
        String kakaoAccessToken = signInRequestDto.getAccessToken();

        // kakaoAccessToken의 유효성 검사
        kakaoTokenService.isValidAccessToken(kakaoAccessToken);

        // SignInRequest의 아이디 얻기
        Long userId = kakaoTokenService.getUserIdByToken(kakaoAccessToken);

        // kakaoToken 정보 저장하기
        KakaoToken kakaoToken = KakaoTokenConverter.fromSignInRequestDto(userId, signInRequestDto);

        // For debugging
        System.out.println("kakaoToken: " + kakaoToken);

        kakaoTokenService.saveKakaoToken(kakaoToken);

        System.out.println("유저 확인 합니다");
        // 이전에 등록한 User인지 확인
        if (!userService.isRegisteredUserByUserId(userId)){
            System.out.println("처음 등록한 유저입니다.");

            KakaoUserInfo kakaoUserInfo = kakaoTokenService.getUserInfoByToken(kakaoAccessToken);
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

        String yat = jwtProvider.generateJwtToken(userId, authority);

        System.out.println("Yat가 성공적으로 만들어졌습니다!" );
        System.out.println("Yat를 보내겠습니다." );

        return SignInResponseVO.builder()
                               .yirangAccessToken(yat)
                               .build();
    }

    @Transactional
    public RefreshYatResponseVO refreshYat(String authorizationHeader){

        // Header Parsing
        String oldYat = ParsingHelper.parseHeader(authorizationHeader);


        // Debugging
        System.out.println("oldYat는 이렇습니다.: " + oldYat);

        // OldYat에서 유저 정보와 role 가져오기
        Long userId = jwtParser.getUserIdFromJwt(oldYat);
        Authority authority = jwtParser.getRoleFromJwt(oldYat);

        System.out.println("userId: " + userId);
        System.out.println("authority: " + authority);

        // KRT의 유효기간을 확인해서 유효하면 아래 과정을 수행하기
        if ( kakaoTokenService.isValidKakaoRefreshToken(userId) ){

            System.out.println("Refresh토큰의 유효기간이 남아있습니다.");

            // User Id 와 role을 바탕으로 만들기
            String newYat = jwtProvider.generateJwtToken(userId, authority);

            return RefreshYatResponseVO.builder()
                                       .yirangAccessToken(newYat)
                                       .build();
        }
        else{
            System.out.println("Refresh 토큰의 유효기간이 만료되었습니다.");
            throw new AlreadyExpiredKakaoRefreshTokenException();
        }
    }
}
