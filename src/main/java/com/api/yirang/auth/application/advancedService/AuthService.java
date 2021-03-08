package com.api.yirang.auth.application.advancedService;

import com.api.yirang.auth.application.basicService.AdminService;
import com.api.yirang.auth.application.basicService.KakaoTokenService;
import com.api.yirang.auth.application.intermediateService.UserService;
import com.api.yirang.auth.domain.jwt.components.JwtParser;
import com.api.yirang.auth.domain.jwt.components.JwtProvider;
import com.api.yirang.auth.domain.jwt.components.JwtValidator;
import com.api.yirang.auth.domain.kakaoToken.dto.KakaoUserInfo;
import com.api.yirang.auth.domain.user.converter.UserConverter;
import com.api.yirang.auth.domain.user.exceptions.AlreadyExistedAdmin;
import com.api.yirang.auth.domain.user.exceptions.AlreadyExistedVolunteer;
import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.auth.presentation.VO.RefreshResponseVO;
import com.api.yirang.auth.presentation.VO.SignInResponseVO;
import com.api.yirang.auth.presentation.dto.FakeSignInRequestDto;
import com.api.yirang.auth.presentation.dto.SignInRequestDto;
import com.api.yirang.auth.support.type.Authority;
import com.api.yirang.auth.support.utils.ParsingHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    // Services DI
    private final KakaoTokenService kakaoTokenService;
    private final UserService userService;

    // JWT DI
    private final JwtProvider jwtProvider;
    private final JwtParser jwtParser;

    // Fake Map
    private static final Map<String, Long> fakeUserMap = new HashMap<>();

    static {
        fakeUserMap.put("volunteer_1", 25158L);
        fakeUserMap.put("volunteer_2", 33419L);
        fakeUserMap.put("volunteer_3", 15614L);
        fakeUserMap.put("volunteer_4", 33286L);
        fakeUserMap.put("admin_1", 14378L);
        fakeUserMap.put("admin_2", 17576L);
        fakeUserMap.put("super_admin_1", 91621L);
    }

    @Transactional
    public SignInResponseVO signin(SignInRequestDto signInRequestDto) {
        final String kakaoAccessToken = signInRequestDto.getAccessToken();

        System.out.println("[AuthService]: kakaoAccessToken은 " + kakaoAccessToken);
        // kakaoAccessToken의 유효성 검사
        kakaoTokenService.isValidAccessToken(kakaoAccessToken);

        // SignInRequest의 아이디 얻기
        Long userId = kakaoTokenService.getUserIdByToken(kakaoAccessToken);

        System.out.println("유저 확인 합니다");

        // KakaoUserInfo 얻기
        KakaoUserInfo kakaoUserInfo = kakaoTokenService.getUserInfoByToken(kakaoAccessToken);


        // 이전에 등록한 User인지 확인
        if (!userService.isRegisteredUserByUserId(userId)){

            User user = UserConverter.fromKakaoUserInfo(userId, kakaoUserInfo, Authority.ROLE_VOLUNTEER);
            userService.saveUser(user);
        }
        else{
            System.out.println("이전에 등록했던 봉사자입니다.");
        }

        // imgUrl 업데이트 하기


        Authority authority = userService.getAuthorityByUserId(userId);

        System.out.println("봉사자의 authority는: " + authority);

        String yat = jwtProvider.generateJwtToken(userId);

        return SignInResponseVO.builder()
                               .yirangAccessToken(yat)
                               .build();
    }

    @Transactional
    public RefreshResponseVO refresh(String header){
        String YAT = ParsingHelper.parseHeader(header);

        System.out.println("[AuthService]: Refresh를 위한 YAT를 받았습니다.: " + YAT);

        Long userId = jwtParser.getUserIdFromJwt(YAT);

        String newYAT = jwtProvider.generateJwtToken(userId);

        System.out.println("[AuthService]: 새로운 YAT를 보내겠습니다.: " + newYAT);

        return RefreshResponseVO.builder()
                                .yirangAccessToken(newYAT)
                                .build();

    }
    @Transactional
    public void changeToAdmin(Long userId){
        if(userService.getAuthorityByUserId(userId)!=Authority.ROLE_VOLUNTEER){
            throw new AlreadyExistedAdmin();
        }
        userService.registerAdmin(userId);
    }

    @Transactional
    public void changeToVolunteer(Long userId){
        if(userService.getAuthorityByUserId(userId)!=Authority.ROLE_ADMIN){
            throw new AlreadyExistedVolunteer();
        }
        userService.fireAdmin(userId);
    }

    @Transactional
    public String fakeSignIn(FakeSignInRequestDto fakeSignInRequestDto) {
        Long userId = fakeUserMap.get(fakeSignInRequestDto.getFakeAuthority());
        return jwtProvider.generateJwtToken(userId);
    }

    // For Test
    // Super Admin은 DB 담당자가 주관합니다.

}
