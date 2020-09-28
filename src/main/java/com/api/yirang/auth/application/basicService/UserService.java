package com.api.yirang.auth.application.basicService;


import com.api.yirang.auth.domain.jwt.components.JwtParser;
import com.api.yirang.auth.domain.user.converter.UserConverter;
import com.api.yirang.auth.domain.user.exceptions.UserNullException;
import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.auth.presentation.dto.RegisterDto;
import com.api.yirang.auth.presentation.dto.UserInfoResponseDto;
import com.api.yirang.auth.repository.persistence.maria.UserDao;
import com.api.yirang.auth.support.type.Authority;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    // DI dao
    private final UserDao userDao;

    // DI jwtUtils
    private final JwtParser jwtParser;

    /*-------------------------------------*/
    // Methods

    // Methods For get or find
    public Authority getAuthorityByUserId(Long userId) {
        User user = userDao.findByUserId(userId).orElse(null);
        return (user == null) ? Authority.ROLE_USER: user.getAuthority();
    }

    public User findUserByUserId(Long userId) {
        return userDao.findByUserId(userId)
                      .orElseThrow(UserNullException::new);
    }

    @Transactional
    public UserInfoResponseDto findUserInfoByUserId(Long userId) {
        User foundedUser = findUserByUserId(userId);

        // for debugging
        System.out.println("[UserService] User를 찾았습니다: " + foundedUser);

        return UserConverter.toUserInfoResponseDto(foundedUser);
    }


    // Methods For Register or Save
    public void saveUser(User user){
        userDao.save(user);
    }

    public void registerAdmin(RegisterDto registerDto){
        User newUser = UserConverter.fromRegisterDto(registerDto, Authority.ROLE_ADMIN);
        userDao.save(newUser);
    }
    public void registerUser(RegisterDto registerDto){
        User newUser = UserConverter.fromRegisterDto(registerDto, Authority.ROLE_USER);
        userDao.save(newUser);
    }

    // Methods for chekcking existence
    public boolean isRegisteredUserByUserId(Long userId){
        return userDao.existsUserByUserId(userId);
    }

}
