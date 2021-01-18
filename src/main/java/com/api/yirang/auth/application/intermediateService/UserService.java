package com.api.yirang.auth.application.intermediateService;

import com.api.yirang.apply.application.ApplyBasicService;
import com.api.yirang.auth.application.basicService.AdminService;
import com.api.yirang.auth.application.basicService.VolunteerBasicService;
import com.api.yirang.auth.domain.user.converter.UserConverter;
import com.api.yirang.auth.domain.user.exceptions.AlreadyExistedAdmin;
import com.api.yirang.auth.domain.user.exceptions.UserNullException;
import com.api.yirang.auth.domain.user.model.Admin;
import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.auth.presentation.dto.UserInfoRequestDto;
import com.api.yirang.auth.presentation.dto.UserInfoResponseDto;
import com.api.yirang.auth.repository.persistence.maria.UserDao;
import com.api.yirang.auth.support.type.Authority;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    // DI Dao
    private final UserDao userDao;

    // DI service
    private final AdminService adminService;
    private final VolunteerBasicService volunteerBasicService;

    private final ApplyBasicService applyBasicService;

    /*-------------------------------------*/

    // Methods For get or find
    public Authority getAuthorityByUserId(Long userId) {
        User user = userDao.findByUserId(userId).orElse(null);
        return (user == null) ? Authority.ROLE_VOLUNTEER: user.getAuthority();
    }

    private User findUserByUserId(Long userId) {
        return userDao.findByUserId(userId).orElseThrow(UserNullException::new);
    }

    public UserInfoResponseDto findUserInfoByUserId(Long userId) {
        User foundedUser = findUserByUserId(userId);

        // for debugging
        System.out.println("[UserService] User를 찾았습니다: " + foundedUser);

        return UserConverter.toUserInfoResponseDto(foundedUser);
    }


    public void updateUserInfoWithUserId(Long userId, UserInfoRequestDto userInfoRequestDto) {
        System.out.println("[UserService] User를 찾았습니다");

        String email = userInfoRequestDto.getEmail();
        String phone = userInfoRequestDto.getPhone();
        String username = userInfoRequestDto.getUsername();

        userDao.updateUserInfo(userId, email, phone, username);
    }

    public void updateAuthority(Long userId, Authority authority){
        userDao.updateAuthority(userId, authority);
    }

    // Methods for chekcking existence
    public boolean isRegisteredUserByUserId(Long userId){
        return userDao.existsUserByUserId(userId);
    }


    // 유저 저장하기
    public void saveUser(User user){
        // 유저의 권한 확인하기
        Authority authority = user.getAuthority();

        if (authority == Authority.ROLE_VOLUNTEER) {
            volunteerBasicService.save(user);
        }
        else {
            adminService.save(user);
        }
    }

    // 일반 user -> admin 추가하기
    public void registerAdmin(Long userId){

        // userId에 해당하는 User가 있는 지 검사
        User user = findUserByUserId(userId);
        // User의 권한 바꾸기
        updateAuthority(userId, Authority.ROLE_ADMIN);

        // Admin에 아이디 추가
        // 예전에 있었떤 Admin이면 그 Number를 계속 씀
        if (!adminService.isExistedByUserId(userId)) {
            adminService.save(user);
        }
    }

    // admin -> 일반 User로 강등
    public void fireAdmin(Long userId) {

        // userId에 해당하는 User가 있는 지 검사
        User user = findUserByUserId(userId);

        // Admin으로 등록된 사람이 맞는지 확인
        Admin admin = adminService.findAdminByUserId(userId);

        // User 권한 바꾸기
        updateAuthority(userId, Authority.ROLE_VOLUNTEER);

        // admin을 지우진 않음
    }

    // DELETE
    public void deleteUser(Long userId) {
        User user = findUserByUserId(userId);

        // 1. Admin이나 Volunteer Data 지우기
        if (user.getAuthority() == Authority.ROLE_ADMIN){
            adminService.delete(user);
        }
        else{
            volunteerBasicService.delete(user);
        }
        // 2. User 삭제
        userDao.delete(user);
    }
}
