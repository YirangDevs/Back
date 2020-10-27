package com.api.yirang.auth.application.intermediateService;

import com.api.yirang.auth.application.basicService.AdminService;
import com.api.yirang.auth.application.basicService.VolunteerService;
import com.api.yirang.auth.domain.user.converter.UserConverter;
import com.api.yirang.auth.domain.user.exceptions.AlreadyExistedAdmin;
import com.api.yirang.auth.domain.user.exceptions.UserNullException;
import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.auth.presentation.dto.UserInfoResponseDto;
import com.api.yirang.auth.repository.persistence.maria.UserDao;
import com.api.yirang.auth.support.type.Authority;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    // DI Dao
    private final UserDao userDao;

    // DI service
    private final AdminService adminService;
    private final VolunteerService volunteerService;

    /*-------------------------------------*/

    // Methods For get or find
    public Authority getAuthorityByUserId(Long userId) {
        User user = userDao.findByUserId(userId).orElse(null);
        return (user == null) ? Authority.ROLE_VOLUNTEER: user.getAuthority();
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


    // 유저 저장하기
    @Transactional
    public void saveUser(User user){
        // 유저의 권한 확인하기
        Authority authority = user.getAuthority();

        if (authority == Authority.ROLE_VOLUNTEER) {
            volunteerService.save(user);
        }
        else {
            adminService.save(user);
        }
    }

    // user -> admin 추가하기
    @Transactional
    public void registerAdmin(Long userId){

        // userId에 해당하는 User가 있는 지 검사 없으면 예외 처리
        User user = userDao.findByUserId(userId).orElseThrow(UserNullException::new);
        // 이미 해당하는 user가 admin에 있으면 예외 처리
        if ( adminService.isExistedUser(user) )
        {
            throw new AlreadyExistedAdmin();
        }
        // Admin에 아이디 추가
        adminService.save(user);
    }

    // Methods for chekcking existence
    public boolean isRegisteredUserByUserId(Long userId){
        return userDao.existsUserByUserId(userId);
    }

}
