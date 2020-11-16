//package com.api.yirang.notice.service;
//
//
//import com.api.yirang.auth.application.basicService.AdminService;
//import com.api.yirang.auth.application.intermediateService.UserService;
//import com.api.yirang.auth.domain.jwt.components.JwtParser;
//import com.api.yirang.auth.domain.user.exceptions.AdminNullException;
//import com.api.yirang.auth.domain.user.model.Admin;
//import com.api.yirang.auth.support.utils.ParsingHelper;
//import com.api.yirang.notices.application.advancedService.NoticeActivityService;
//import com.api.yirang.notices.application.basicService.ActivityService;
//import com.api.yirang.notices.application.basicService.NoticeService;
//import com.api.yirang.notices.domain.activity.exception.ActivityNullException;
//import com.api.yirang.notices.domain.activity.exception.AlreadyExistedActivityException;
//import com.api.yirang.notices.domain.activity.model.Activity;
//import com.api.yirang.notices.domain.notice.exception.AlreadyExistedNoticeException;
//import com.api.yirang.notices.domain.notice.exception.NoticeNullException;
//import com.api.yirang.notices.domain.notice.model.Notice;
//import com.api.yirang.notices.presentation.dto.NoticeRegisterRequestDto;
//import com.api.yirang.notices.presentation.dto.embeded.ActivityRegisterRequestDto;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.Collection;
//import java.util.HashSet;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@RunWith(SpringRunner.class)
//@ActiveProfiles("test")
//public class NoticeActivityCreateTest {
//
//    // Test 하고자 하는 service
//    @Autowired
//    NoticeActivityService noticeActivityService;
//
//    // 보조
//    @Autowired
//    NoticeService noticeService;
//    @Autowired
//    ActivityService activityService;
//    @Autowired
//    AdminService adminService;
//    @Autowired
//    UserService userService;
//    @Autowired
//    JwtParser jwtParser;
//
//    // Test variables
//    String[] titles = {"Title1", "Title2", "Title3", "Title4"};
//    Long[] nors = {Long.valueOf(5), Long.valueOf(5), Long.valueOf(5), Long.valueOf(5)};
//    String[] dovs = {"2020-02-10", "2020-02-11", "2020-02-12", "2020-02-13"};
//    String[] tovs = {"11:10:11", "11:10:12", "11:10:13", "11:10:14"};
//    String[] dods = {"2020-02-09", "2020-02-08","2020-02-08","2020-02-08"};
//    String[] regions = {"중구", "서구", "남구", "수성구"};
//    String[] contents = {"GOOD", "GOOD","GOOD","GOOD"};
//
//    Collection<Long> notices = new HashSet<>();
//    Collection<Long> activities = new HashSet<>();
//
//    @Value("${test.api.token.yirang_access_token}")
//    String YAT;
//
//    @Before
//    public void setUp(){
//        System.out.println("테스트가 시작합니다.");
//        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader("Bearer " + YAT));
//        if (!adminService.isExistedByUserId(userId) ){
//            userService.registerAdmin(userId);
//        }
//        noticeService.deleteAll();
//        activityService.deleteAll();
//    }
//
//    @After
//    public void tearDown(){
//        System.out.println("테스트가 끝났습니다.");
//        // Notice 지우기
////        Iterator<Long> noticeItr = notices.iterator();
////        while(noticeItr.hasNext()){
////            Long noticeId = noticeItr.next();
////            noticeService.deleteNoticeByNoticeId(noticeId);
////        }
////        Iterator<Long> activityItr = activities.iterator();
////        while(activityItr.hasNext()){
////            Long activityId = activityItr.next();
////            activityService.deleteOnlyActivityById(activityId);
////        }
////        notices.clear();
////        activities.clear();
//    }
//
//    protected void saveAndCheck(String header, String title, String content, String region,
//                        String dod, String tov, String dov,
//                        Long nor){
//        ActivityRegisterRequestDto activityRegisterRequestDto =
//                ActivityRegisterRequestDto.builder()
//                                          .content(content)
//                                          .region(region)
//                                          .dod(dod).tov(tov).dov(dov)
//                                          .nor(nor)
//                                          .build();
//
//        NoticeRegisterRequestDto noticeRegisterRequestDto =
//                NoticeRegisterRequestDto.builder()
//                                        .title(title)
//                                        .activityRegisterRequestDto(activityRegisterRequestDto)
//                                        .build();
//        System.out.println(noticeRegisterRequestDto);
//        noticeActivityService.registerNew(header, noticeRegisterRequestDto);
//
//        Region regionModel = regionService.findRegionByRegionName(region);
//        Activity activity = activityService.findActivityByRegionAndDTOV(regionModel, dov, tov);
//        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
//        Notice notice = noticeService.findByNoticeTitle(title);
//        notices.add(notice.getNoticeId());
//        activities.add(activity.getActivityId());
//
//        // Activity 저장 됬는지 확인하기
//        assertThat(activity.getNor()).isEqualTo(nor);
//        assertThat(activity.getNoa()).isEqualTo(Long.valueOf(0));
//        assertThat(activity.getContent()).isEqualTo(content);
//        assertThat(activity.getRegion().getRegionId()).isEqualTo(regionModel.getRegionId());
//
//        // Notice 저장 됬는 지 확인하기
//        assertThat(notice.getTitle()).isEqualTo(title);
//        assertThat(notice.getAdmin().getAdminNumber()).isEqualTo(adminService.findAdminByUserId(userId).getAdminNumber());
//        assertThat(notice.getActivity().getActivityId()).isEqualTo(activity.getActivityId());
//    }
//    public void urgentSaveAndCheck(String header, Long noticeId, String title){
//        noticeActivityService.registerUrgent(header, noticeId, title);
//        Notice notice = noticeService.findByNoticeTitle(title);
//        // 나중에 삭제하기 위해서
//        notices.add(notice.getNoticeId());
//
//        // 작성자 확인
//        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
//        Admin admin = notice.getAdmin();
//        assertThat(admin.getAdminNumber()).isEqualTo(adminService.findAdminByUserId(userId).getAdminNumber());
//
//        // Activity 그대로인지 확인
//        Notice originNotice = noticeService.findByNoticeId(noticeId);
//        Activity originActivity = originNotice.getActivity();
//
//        Activity activity = notice.getActivity();
//        assertThat(originActivity.getActivityId()).isEqualTo(activity.getActivityId());
//    }
//
//
//    @Test
//    public void 정상적으로_하나_등록했을_떄(){
//        saveAndCheck("Bearer " + YAT, titles[0], contents[0], regions[0],
//                     dods[0], tovs[0], dovs[0], nors[0]);
//    }
//    @Test
//    public void 여러개_저장할_떄(){
//        // 저장
//        for(int i =0; i < 4; i++){
//            saveAndCheck("Bearer " + YAT, titles[i], contents[i], regions[i],
//                 dods[i], tovs[i], dovs[i], nors[i]);
//        }
//    }
//    @Test(expected = AlreadyExistedNoticeException.class)
//    public void 중복된_제목의_공고가_저장될_때(){
//        saveAndCheck("Bearer " + YAT, titles[0], contents[0], regions[0],
//                     dods[0], tovs[0], dovs[0], nors[0]);
//        saveAndCheck("Bearer " + YAT, titles[0], contents[0], regions[1],
//                     dods[2], tovs[0], dovs[0], nors[0]);
//    }
//    @Test(expected = AlreadyExistedActivityException.class)
//    public void 중복된_봉사활동이_저장될_때(){
//        saveAndCheck("Bearer " + YAT, titles[0], contents[0], regions[0],
//                     dods[0], tovs[0], dovs[0], nors[0]);
//        saveAndCheck("Bearer " + YAT, titles[1], contents[2], regions[0],
//                     dods[0], tovs[0], dovs[0], nors[0]);
//    }
//    @Test(expected = AdminNullException.class)
//    public void 공고글_등록시_작성자에_해당하는_Admin이_없으면(){
//        // admin 삭제
//        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader("Bearer " + YAT));
//        userService.fireAdmin(userId);
//        saveAndCheck("Bearer " + YAT, titles[0], contents[0], regions[0],
//                     dods[0], tovs[0], dovs[0], nors[0]);
//    }
//
//    @Test
//    public void 정상적인_관리자_긴급_공고_등록(){
//        //먼저 기존의 공고글 이 있고
//        saveAndCheck("Bearer " + YAT, titles[0], contents[0], regions[0],
//                     dods[0], tovs[0], dovs[0], nors[0]);
//        Notice notice = noticeService.findByNoticeTitle(titles[0]);
//        Long noticeId = notice.getNoticeId();
//
//        urgentSaveAndCheck("Bearer " + YAT, noticeId, "긴급 공고입니다.");
//    }
//    @Test
//    public void 정상적인_관리자_긴급_공고_여러_개_등록(){
//        saveAndCheck("Bearer " + YAT, titles[0], contents[0], regions[0],
//                     dods[0], tovs[0], dovs[0], nors[0]);
//        Notice notice = noticeService.findByNoticeTitle(titles[0]);
//        Long noticeId = notice.getNoticeId();
//
//        urgentSaveAndCheck("Bearer " + YAT, noticeId, "긴급 공고입니다1.");
//        urgentSaveAndCheck("Bearer " + YAT, noticeId, "긴급 공고입니다2.");
//        urgentSaveAndCheck("Bearer " + YAT, noticeId, "긴급 공고입니다3.");
//    }
//    @Test(expected = AlreadyExistedNoticeException.class)
//    public void 중복되는_긴급_공고_등록(){
//        saveAndCheck("Bearer " + YAT, titles[0], contents[0], regions[1],
//                     dods[0], tovs[0], dovs[0], nors[0]);
//        Notice notice = noticeService.findByNoticeTitle(titles[0]);
//        Long noticeId = notice.getNoticeId();
//
//        urgentSaveAndCheck("Bearer " + YAT, noticeId, "긴급 공고입니다1.");
//        urgentSaveAndCheck("Bearer " + YAT, noticeId, "긴급 공고입니다1.");
//    }
//    @Test(expected = NoticeNullException.class)
//    public void 긴급공고_등록_시_Notice가_없을_때(){
//        urgentSaveAndCheck("Bearer " + YAT, Long.valueOf(0), "긴급 공고입니다1.");
//    }
//    @Test(expected = ActivityNullException.class)
//    public void 긴급공고_등록시_activity가_없을_떄(){
//        saveAndCheck("Bearer " + YAT, titles[0], contents[0], regions[0],
//                     dods[0], tovs[0], dovs[0], nors[0]);
//        Notice notice = noticeService.findByNoticeTitle(titles[0]);
//        Long noticeId = notice.getNoticeId();
//
//        // acitiy 삭제
//        Activity activity = notice.getActivity();
//        Long activityId = activity.getActivityId();
//        activityService.deleteOnlyActivityById(activityId);
//        activities.remove(activityId);
//
//        urgentSaveAndCheck("Bearer " + YAT, noticeId, "긴급 공고입니다1.");
//    }
//    @Test(expected = AdminNullException.class)
//    public void 긴급공고_등록_시_Admin이_없으면(){
//        saveAndCheck("Bearer " + YAT, titles[0], contents[0], regions[0],
//                     dods[0], tovs[0], dovs[0], nors[0]);
//        Notice notice = noticeService.findByNoticeTitle(titles[0]);
//        Long noticeId = notice.getNoticeId();
//
//        // admin 삭제
//        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader("Bearer " + YAT));
//        userService.fireAdmin(userId);
//
//        urgentSaveAndCheck("Bearer " + YAT, noticeId, "긴급 공고입니다1.");
//    }
//}
//
//
//
