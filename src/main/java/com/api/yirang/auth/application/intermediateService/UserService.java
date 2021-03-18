package com.api.yirang.auth.application.intermediateService;

import com.api.yirang.auth.application.basicService.AdminService;
import com.api.yirang.auth.application.basicService.VolunteerBasicService;
import com.api.yirang.auth.domain.user.converter.UserConverter;
import com.api.yirang.auth.domain.user.exceptions.UserNullException;
import com.api.yirang.auth.domain.user.model.Admin;
import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.auth.presentation.dto.UserAuthResponseDto;
import com.api.yirang.auth.presentation.dto.UserInfoRequestDto;
import com.api.yirang.auth.presentation.dto.UserInfoResponseDto;
import com.api.yirang.auth.repository.persistence.maria.UserDao;
import com.api.yirang.auth.support.type.Authority;
import com.api.yirang.common.support.type.Region;
import com.api.yirang.common.support.type.Sex;
import com.api.yirang.email.dto.EmailRequestDto;
import com.api.yirang.email.exception.EmailAddressNullException;
import com.api.yirang.email.exception.EmailDuplicatedException;
import com.api.yirang.email.exception.EmailNullException;
import com.api.yirang.email.model.Email;
import com.api.yirang.email.repository.EmailRepository;
import com.api.yirang.email.util.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    // DI Dao
    private final UserDao userDao;
    private final EmailRepository emailRepository;

    // DI service
    private final AdminService adminService;
    private final VolunteerBasicService volunteerBasicService;

    /*-------------------------------------*/

    // Methods For get or find
    public Authority getAuthorityByUserId(Long userId) {
        User user = userDao.findByUserId(userId).orElse(null);
        return (user == null) ? Authority.ROLE_VOLUNTEER: user.getAuthority();
    }

    public String getEmailByUserId(Long userId){
        User user = userDao.findByUserId(userId).orElse(null);
        return (user == null) ? null : user.getEmail();
    }

    public User findUserByUserId(Long userId) {
        return userDao.findByUserId(userId).orElseThrow(UserNullException::new);
    }

    public UserInfoResponseDto findUserInfoByUserId(Long userId) {
        User foundedUser = findUserByUserId(userId);

        Email email = emailRepository.findEmailByUser_UserId(userId).orElseThrow(new EmailNullException(userId));
        // for debugging
        System.out.println("[UserService] User를 찾았습니다: " + foundedUser);


        return UserConverter.toUserInfoResponseDto(foundedUser, email.getNotifiable());
    }

    public Collection<UserAuthResponseDto> findAllUserAuthInfos() {

        Collection<UserAuthResponseDto> res = new ArrayList<>();

        for(User user: userDao.findAll()){
            res.add(
                    (user.getAuthority() == Authority.ROLE_VOLUNTEER) ?
                            UserConverter.toUserAuthResponseDto(user, null) :
                            UserConverter.toUserAuthResponseDto(user, adminService.findAreasByUserId(user.getUserId()))
            );
        }
        return res;
    }

    public void updateUserInfoWithUserId(Long userId, UserInfoRequestDto userInfoRequestDto) {
        System.out.println("[UserService] User를 업데이트 합니다.");

        String username = userInfoRequestDto.getUsername();
        String realname = userInfoRequestDto.getRealname();
        String phone = userInfoRequestDto.getPhone();
        Sex sex = userInfoRequestDto.getSex();

        String email = userInfoRequestDto.getEmail();

        Region firstRegion = userInfoRequestDto.getFirstRegion();
        Region secondRegion = userInfoRequestDto.getSecondRegion();

        userDao.updateUserInfo(userId, email, phone, username, realname, sex, firstRegion, secondRegion);
    }

    public void updateAuthority(Long userId, Authority authority){
        userDao.updateAuthority(userId, authority);
    }

    // Methods for chekcking existence
    public boolean isRegisteredUserByUserId(Long userId){
        return userDao.existsUserByUserId(userId);
    }

    public void updateMyEmail(Long userId, EmailRequestDto emailRequestDto) {
        // User 찾기
        User user = findUserByUserId(userId);
        // 기존의 email과 같은 경우
        if (user.getEmail().equals(emailRequestDto.getEmail())){
            throw new EmailDuplicatedException();
        }

        // userDao로 email 업데이트 하기
        userDao.updateUserInfo(user.getUserId(), emailRequestDto.getEmail(), user.getPhone(), user.getUsername(),
                               user.getRealname(), user.getSex(), user.getFirstRegion(), user.getSecondRegion());
        // verify 무효화 하기
        emailRepository.updateEmailVerificationWithUserId(userId, Validation.VALIDATION_NO);

    }


    // 유저 저장하기
    public void saveUser(User newUser){
        // 유저의 권한 확인하기
        Authority authority = newUser.getAuthority();
        // 유저 저장
        User user = userDao.save(newUser);
        // 유저 Email 테이블 추가
        emailRepository.save(Email.builder()
                                  .user(user)
                                  .build());

        if (authority == Authority.ROLE_VOLUNTEER) {
            volunteerBasicService.save(user);
        }
        else {
            adminService.save(user);
        }
    }

    // Volunteer -> Admin로 승급
    public void registerAdmin(Long userId){

        // userId에 해당하는 User가 있는 지 검사
        User user = findUserByUserId(userId);
        // User의 권한 바꾸기
        updateAuthority(userId, Authority.ROLE_ADMIN);

        // Admin에 아이디 추가
        // 예전에 있었 Admin이면 그 Number를 계속 씀
        if (!adminService.isExistedByUserId(userId)) {
            adminService.save(user);
        }
    }

    // admin -> Volunteer 로 강등
    public void fireAdmin(Long userId) {

        // userId에 해당하는 User가 있는 지 검사
        User user = findUserByUserId(userId);

        // Admin으로 등록된 사람이 맞는지 확인
        Admin admin = adminService.findAdminByUserId(userId);

        // Admin 지역 초기화
        adminService.updateRegionsByUserId(userId, null);

        // User 권한 바꾸기
        updateAuthority(userId, Authority.ROLE_VOLUNTEER);

        // admin을 지우진 않음
    }

    // [TEST]용
    // Admin -> Super Admin
    public void upgradeSuper(Long userId){
        User user = findUserByUserId(userId);

        // Admin으로 등록된 사람이 맞는지 확인
        Admin admin = adminService.findAdminByUserId(userId);
        updateAuthority(userId, Authority.ROLE_SUPER_ADMIN);
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