package com.api.yirang.email.application;

import com.api.yirang.auth.application.basicService.AdminService;
import com.api.yirang.auth.application.basicService.VolunteerBasicService;
import com.api.yirang.auth.application.intermediateService.UserIntermediateService;
import com.api.yirang.auth.application.intermediateService.UserService;
import com.api.yirang.auth.domain.user.exceptions.UserNullException;
import com.api.yirang.auth.domain.user.model.Admin;
import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.auth.domain.user.model.Volunteer;
import com.api.yirang.auth.repository.persistence.maria.UserDao;
import com.api.yirang.auth.support.type.Authority;
import com.api.yirang.common.support.type.Region;
import com.api.yirang.email.dto.ActivityGuideMailContent;
import com.api.yirang.email.dto.MatchingMailContent;
import com.api.yirang.email.dto.NoticeRecommendMailContent;
import com.api.yirang.email.dto.UserWithdrawMailContent;
import com.api.yirang.email.repository.APIrepository;
import com.api.yirang.img.application.StaticMapService;
import com.api.yirang.matching.application.GeoCodeService;
import com.api.yirang.matching.application.MatchingCrudService;
import com.api.yirang.matching.model.maria.Matching;
import com.api.yirang.matching.model.mongo.UnMatchingList;
import com.api.yirang.notices.application.basicService.ActivityBasicService;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.notices.domain.notice.model.Notice;
import com.api.yirang.seniors.application.basicService.VolunteerServiceBasicService;
import com.api.yirang.seniors.domain.senior.model.Senior;
import com.api.yirang.seniors.domain.volunteerService.model.VolunteerService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
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
    private final APIrepository apIrepository;

    private final AdminService adminService;
    private final VolunteerServiceBasicService volunteerServiceBasicService;
    private final EmailService emailService;
    private final MatchingCrudService matchingCrudService;
    private final VolunteerBasicService volunteerBasicService;
    private final UserService userService;
    private final StaticMapService staticMapService;
    private final GeoCodeService geoCodeService;


    @Async("threadPoolTaskExecutor")
    public void sendEmailToAdminAboutMatching(Activity activity) {
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
        List<Senior> seniors = volunteerServices.stream()
                                                .map(VolunteerService::getSenior)
                                                .collect(Collectors.toList());
        // 4. volunteer mail에 들어갈 LIST 만들기
        List<MatchingMailContent> matchingMailContentList = seniors.stream()
                                                                   .map(senior -> MatchingMailContent.builder()
                                                                                                     .dtov(activity.getDtov())
                                                                                                     .region(region)
                                                                                                     .phoneNumber(senior.getPhone())
                                                                                                     .seniorName(senior.getSeniorName())
                                                                                                     .build())
                                                                   .collect(Collectors.toList());
        // log
        System.out.println("MatchingMailContentList: " + matchingMailContentList);

        //5. 이메일 보내기
        adminList.forEach(admin -> emailService.sendMatchingEmail(admin.getUser()
                                                                       .getUserId(), matchingMailContentList));
    }

    @Async("threadPoolTaskExecutor")
    public void sendEmailToAdminAboutUserWithdraw(Long requestId, Long userId) {

        // 0. 유저 찾기
        User user = userDao.findByUserId(userId)
                           .orElseThrow(UserNullException::new);
        User requestUser = userDao.findByUserId(requestId)
                                  .orElseThrow(UserNullException::new);

        final String type = requestId.equals(userId) ? "[본인]" :
                requestUser.getAuthority()
                           .equals(Authority.ROLE_ADMIN) ? "[관리자]" : "[슈퍼_관리자]";

        final String requestUsername = (requestUser.getRealname() == null ?
                requestUser.getUsername() : requestUser.getRealname()) + type;


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
                                                                                 .name(user.getRealname() ==
                                                                                       null ? user.getUsername() : user.getRealname())
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
                                                                                                            .dtov(activity.getDtov())
                                                                                                            .region(activity.getRegion())
                                                                                                            .phoneNumber(senior.getPhone())
                                                                                                            .seniorName(senior.getSeniorName())
                                                                                                            .build();
                                                                              })
                                                                              .collect(Collectors.toList());


        // 5. 해당하는 관리자들에게 이메일 보내기
        adminList.forEach(admin -> emailService.sendWithdrawEmail(admin.getUser()
                                                                       .getUserId(), userWithdrawMailContent, matchingMailContentList));
    }

    @Async("threadPoolTaskExecutor")
    public void sendEmailToVolunteerAboutTomorrowActivity(Activity activity) {

        //1. activity에 해당하는 MatchingList 찾기
        List<Matching> matchingList = matchingCrudService.findMatchingsByActivity(activity, true);
        //2. activity에 해당하는 UnMatchingList의 Volunteer 찾기
        List<Volunteer> unMatchedVolunteers = matchingCrudService.findUnmatchingByActivityId(activity.getActivityId())
                                                                 .getVolunteerIds()
                                                                 .stream()
                                                                 .map(volunteerBasicService::findVolunteerByVolunteerNumber)
                                                                 .collect(Collectors.toList());

        //3. 매칭 성공한 사람들에게 안내 메일 보내기
        matchingList.forEach(this::sendEmailToVolunteerAboutSuccessEmail);
        //4. 매칭 실패한 사람들에게 실패 메일 보내기
        unMatchedVolunteers.forEach(this::sendEmailToVolunteerAboutFailedEmail);
    }

    @Async("threadPoolTaskExecutor")
    public void sendEmailToVolunteerAboutRecommendedActivity(Notice notice) {

        // 0. Region
        Activity activity = notice.getActivity();
        Region region = notice.getActivity().getRegion();

        // 1. 보내야할 User 찾기
        List<User> userList = userService.findUserWhoAreVolunteerAndLoveRegion(region);
        System.out.println("userList: " + userList);

        NoticeRecommendMailContent noticeRecommendMailContent = NoticeRecommendMailContent.builder()
                                                                                          .region(region)
                                                                                          .noticeTitle(notice.getTitle())
                                                                                          .dtod(activity.getDtod())
                                                                                          .dtov(activity.getDtov())
                                                                                          .nor(activity.getNor())
                                                                                          .build();

        userList.forEach(user -> emailService.sendRegionRecommendEmail(user.getUserId(), noticeRecommendMailContent));
    }

    public void sendEmailToVolunteerAboutSuccessEmail(Matching matching){
        // 필요한 거:
        //1. content에 들어갈 것들 만들기
        Volunteer volunteer = matching.getVolunteer();
        Activity activity = matching.getActivity();
        Senior senior = matching.getSenior();

        String roadAddress = senior.getAddress();
        String areaAddress = apIrepository.getJibunFromAPI(roadAddress);

        //네이버 지도를 위한 1. GeoCoding으로 위도 경도 구하기 + 2. Mongo DB로 지도 넣기
        ActivityGuideMailContent activityGuideMailContent = ActivityGuideMailContent.builder()
                                                                                    .volunteerName(volunteer.getUser().getRealname())
                                                                                    .activityTime(activity.getDtov())
                                                                                    .region(activity.getRegion())
                                                                                    .roadAddress(roadAddress)
                                                                                    .areaAddress(areaAddress)
                                                                                    .serviceType(matching.getServiceType())
                                                                                    .seniorSex(senior.getSex())
                                                                                    .seniorPhone(senior.getPhone())
                                                                                    .seniorName(senior.getSeniorName())
                                                                                    .geoCode(geoCodeService.getGeoCode(roadAddress))
                                                                                    .staticMapUrl(staticMapService.findOrPullStaticMap(roadAddress))
                                                                                    .build();


        //2. email 보내기
        emailService.sendGuideEmail(matching.getVolunteer().getUser().getUserId(), activityGuideMailContent);

    }

    @Async("threadPoolTaskExecutor")
    public void sendEmailToVolunteerAboutFailedEmail(Volunteer volunteer){

        //1. 유저 이름
        User user = volunteer.getUser();
        // 2. 이메일 보내기
        emailService.sendFailEmail(user.getUserId());
    }

}
