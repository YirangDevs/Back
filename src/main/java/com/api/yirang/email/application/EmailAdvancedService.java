package com.api.yirang.email.application;

import com.api.yirang.auth.application.basicService.AdminService;
import com.api.yirang.auth.application.intermediateService.UserService;
import com.api.yirang.auth.domain.user.exceptions.UserNullException;
import com.api.yirang.auth.domain.user.model.Admin;
import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.auth.repository.persistence.maria.UserDao;
import com.api.yirang.auth.support.type.Authority;
import com.api.yirang.common.support.type.Region;
import com.api.yirang.email.dto.MatchingMailContent;
import com.api.yirang.email.dto.UserWithdrawMailContent;
import com.api.yirang.matching.application.MatchingCrudService;
import com.api.yirang.matching.model.maria.Matching;
import com.api.yirang.notices.application.basicService.ActivityBasicService;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.seniors.application.basicService.VolunteerServiceBasicService;
import com.api.yirang.seniors.domain.senior.model.Senior;
import com.api.yirang.seniors.domain.volunteerService.model.VolunteerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailAdvancedService {


    // Dao
    private final UserDao userDao;

    private final AdminService adminService;
    private final VolunteerServiceBasicService volunteerServiceBasicService;
    private final EmailService emailService;
    private final MatchingCrudService matchingCrudService;

    public void sendEmailToAdminAboutMatching(Activity activity){
        // 1. activity의 지역 알아내기
        Region region = activity.getRegion();
        // log
        System.out.println("Region: " + region);
        // 2. 각 지역의 admin, super_admin을 알아내기
        List<Admin> adminList = adminService.findAdminsByRegion(region);

        // log
        System.out.println("AdminList: " + adminList);
        // 3. 해당 봉사 지역의 어르신들 구하기
        Collection<VolunteerService> volunteerServices = volunteerServiceBasicService.findVolunteerServicesByActivity(activity);
        List<Senior> seniors = volunteerServices.stream().map(VolunteerService::getSenior).collect(Collectors.toList());
        // 4. volunteer mail에 들어갈 LIST 만들기
        List<MatchingMailContent> matchingMailContentList = seniors.stream()
                                                                   .map(senior -> MatchingMailContent.builder()
                                                                                                .dtov(activity.getDtov()).region(region)
                                                                                                .phoneNumber(senior.getPhone())
                                                                                                .seniorName(senior.getSeniorName())
                                                                                                .build())
                                                                   .collect(Collectors.toList());
        // log
        System.out.println("MatchingMailContentList: " + matchingMailContentList);

        //5. 이메일 보내기
        adminList.forEach(admin -> emailService.sendMatchingEmail(admin.getUser().getUserId(), matchingMailContentList));
    }

    public void sendEmailToAdminAboutUserWithdraw(Long requestId, Long userId) {

        // 0. 유저 찾기
        User user = userDao.findByUserId(userId).orElseThrow(UserNullException::new);
        User requestUser = userDao.findByUserId(requestId).orElseThrow(UserNullException::new);

        final String type = requestId.equals(userId) ? "[본인]" :
                            requestUser.getAuthority().equals(Authority.ROLE_ADMIN) ? "[관리자]" : "[슈퍼_관리자]";

        final String requestUsername = ( requestUser.getRealname() == null ?
                                            requestUser.getUsername() : requestUser.getRealname() ) + type;


        // 1. userId로 매칭되어있는 현재 진행 중인 매칭 데이터 가져오기
        List<Matching> matchingList = matchingCrudService.findMatchingOnProcessByUserId(userId);

        // 2. 각 지역에 해당하는 admin, super_admin 알아내기
        List<Admin> adminList = new ArrayList<>();
        matchingList.stream()
                    .map(Matching::getActivity)
                    .map(Activity::getRegion)
                    .forEach(region -> adminList.addAll(adminService.findAdminsByRegion(region)));

        // 3. User 정보 담긴 MailContent 클래스 만들기
        UserWithdrawMailContent userWithdrawMailContent = UserWithdrawMailContent.builder()
                                                                                 .requestUsername(requestUsername)
                                                                                 // 기본은 realname으로 없으면, username으로
                                                                                 .name(user.getRealname() == null ? user.getUsername() : user.getRealname())
                                                                                 .authority(user.getAuthority())
                                                                                 .sex(user.getSex())
                                                                                 .phoneNumber(user.getPhone())
                                                                                 .email(user.getEmail())
                                                                                 .build();
        // 4. 매칭 Content List 만들기.
        final List<MatchingMailContent> matchingMailContentList = matchingList.stream()
                                                                              .map(matching -> {
                                                                                        Activity activity = matching.getActivity();
                                                                                        Senior senior = matching.getSenior();
                                                                                        return MatchingMailContent.builder()
                                                                                                                  .dtov(activity.getDtov()).region(activity.getRegion())
                                                                                                                  .phoneNumber(senior.getPhone())
                                                                                                                  .seniorName(senior.getSeniorName())
                                                                                                                  .build();
                                                                                    })
                                                                             .collect(Collectors.toList());


        // 5. 해당하는 관리자들에게 이메일 보내기
        adminList.forEach(admin -> emailService.sendWithdrawEmail(admin.getUser().getUserId(), userWithdrawMailContent ,matchingMailContentList));
    }
}
