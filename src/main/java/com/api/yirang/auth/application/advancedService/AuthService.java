package com.api.yirang.auth.application.advancedService;

import com.api.yirang.auth.application.basicService.KakaoTokenService;
import com.api.yirang.auth.application.basicService.UserService;
import com.api.yirang.auth.domain.jwt.JwtParser;
import com.api.yirang.auth.domain.jwt.JwtProvider;
import com.api.yirang.auth.domain.jwt.JwtValidator;
import com.api.yirang.auth.domain.kakaoToken.dto.KakaoUserInfo;
import com.api.yirang.auth.domain.kakaoToken.model.KakaoToken;
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
        String kakaoRefreshToken = signInRequestDto.getRefreshToken();
        String kakaoExpiredTime = MyLocalTime.makeExpiredTimeString(signInRequestDto.getRefreshTokenExpiredTime());

        // kakaoAccessToken의 유효성 검사
        kakaoTokenService.isValidAccessToken(kakaoAccessToken);

        // SignInRequest의 아이디 얻기
        Long userId = kakaoTokenService.getUserIdByToken(kakaoAccessToken);

        // kakaoToken 정보 저장하기
        KakaoToken kakaoToken = KakaoToken.builder()
                                          .userId(userId)
                                          .kakaoAccessToken(kakaoAccessToken)
                                          .kakaoRefreshToken(kakaoRefreshToken)
                                          .kakaoRefreshExpiredTime(
                                                  TimeConverter.StringToLocalDateTime(kakaoExpiredTime))
                                          .build();

        // For debugging
        System.out.println("kakaoToken: " + kakaoToken);

        kakaoTokenService.saveKakaoToken(kakaoToken);

        System.out.println("유저 확인 합니다");
        // 이전에 등록한 User인지 확인
        if (!userService.isRegisteredUserByUserId(userId)){
            System.out.println("처음 등록한 유저입니다.");
            // 처음 등록한 사람이면 kakaoUserInfo 얻어오기
            KakaoUserInfo kakaoUserInfo = kakaoTokenService.getUserInfoByToken(kakaoAccessToken);
            // User 정보 저장하기
            User user = User.builder()
                            .userId(userId)
                            .username(kakaoUserInfo.getUsername())
                            .fileUrl(kakaoUserInfo.getFileUrl())
                            .sex(kakaoUserInfo.getSex())
                            .email(kakaoUserInfo.getEmail())
                            .authority(Authority.ROLE_USER)
                            .build();

            // for debugging
            System.out.println("유저: " + user);
            // TO-DO 유정 종보 저장하기
        }
        System.out.println("authority 판단합니다.");
        // User의 authority 판단 하기
        Authority authority = userService.getAuthorityByUserId(userId);

        // 얻은 정보로 YAT(yirang access Token 생성)
        String yat = jwtProvider.generateJwtToken(userId, authority);

        return SignInResponseVO.builder()
                               .yirangAccessToken(yat)
                               .build();
    }

    @Transactional
    public RefreshYatResponseVO refreshYat(String authorizationHeader){

        // Header Parsing
        String oldYat = ParsingHelper.parseHeader(authorizationHeader);

        // OldYat에서 유저 정보와 role 가져오기
        Long id = jwtParser.getUserIdFromJwt(oldYat);
        Authority authority = jwtParser.getRoleFromJwt(oldYat);

        String newYat = jwtProvider.generateJwtToken(id, authority);
        // User Id 와 role을 바탕으로 만들기
        return RefreshYatResponseVO.builder()
                                   .yirangAccessToken(newYat)
                                   .build();
    }
}
