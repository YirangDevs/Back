package com.api.yirang.auth.application.intermediateService;

import com.api.yirang.apply.repository.persistence.maria.ApplyDao;
import com.api.yirang.auth.application.basicService.AdminService;
import com.api.yirang.auth.application.basicService.VolunteerBasicService;
import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.auth.repository.persistence.maria.UserDao;
import com.api.yirang.auth.support.type.Authority;
import com.api.yirang.email.application.EmailAdvancedService;
import com.api.yirang.email.repository.EmailRepository;
import com.api.yirang.img.repository.ImgRepository;
import com.api.yirang.matching.repository.maria.MatchingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class UserIntermediateService {

    // DI Dao
    private final UserDao userDao;
    private final EmailRepository emailRepository;
    private final ImgRepository imgRepository;
    private final MatchingRepository matchingRepository;
    private final ApplyDao applyDao;

    // DI service
    private final AdminService adminService;
    private final VolunteerBasicService volunteerBasicService;
    private final EmailAdvancedService emailAdvancedService;
    private final UserService userService;

    // DELETE
    public void deleteUser(Long userId) {
        User user = userService.findUserByUserId(userId);

        // 1. Admin이나 Volunteer Data 지우기
        if (user.getAuthority().equals(Authority.ROLE_ADMIN)){
            adminService.delete(user);
        }
        else{
            //2. 안내 메일 날리기
            if(matchingRepository.existsMatchingByVolunteer_User_UserIdAndActivity_DtovAfterNow(userId, LocalDateTime.now()))
            {
                emailAdvancedService.sendEmailToAdminAboutUserWithdraw(userId);
            }
            // 3. Matching 삭제
            matchingRepository.deleteAllByVolunteer_User_UserId(userId);
            // 4. Apply 삭제
            applyDao.deleteAllByVolunteer_User_UserId(userId);
            volunteerBasicService.delete(user);
        }
        // 5. Email 삭제
        emailRepository.deleteEmailByUser_UserId(userId);
        // 6. img 삭제
        imgRepository.deleteImgByUser_UserId(userId);
        // 7. User 삭제
        userDao.delete(user);
    }
}
