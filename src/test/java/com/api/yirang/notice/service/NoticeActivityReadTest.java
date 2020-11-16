//package com.api.yirang.notice.service;
//
//
//import com.api.yirang.auth.application.basicService.AdminService;
//import com.api.yirang.auth.application.intermediateService.UserService;
//import com.api.yirang.auth.domain.jwt.components.JwtParser;
//import com.api.yirang.auth.domain.user.model.Admin;
//import com.api.yirang.auth.support.utils.ParsingHelper;
//import com.api.yirang.common.support.time.TimeConverter;
//import com.api.yirang.notices.application.advancedService.NoticeActivityService;
//import com.api.yirang.notices.application.basicService.ActivityService;
//import com.api.yirang.notices.application.basicService.NoticeService;
//import com.api.yirang.notices.domain.activity.model.Activity;
//import com.api.yirang.notices.domain.notice.exception.NoticeNullException;
//import com.api.yirang.notices.domain.notice.model.Notice;
//import com.api.yirang.notices.presentation.dto.NoticeOneResponseDto;
//import com.api.yirang.notices.presentation.dto.NoticeRegisterRequestDto;
//import com.api.yirang.notices.presentation.dto.NoticeResponseDto;
//import com.api.yirang.notices.presentation.dto.embeded.ActivityRegisterRequestDto;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.Iterator;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.Assert.assertTrue;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@TestPropertySource("classpath:properties/application-test.properties")
//public class NoticeActivityReadTest {
//
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
//    String[] titles = {"Title1", "Title2", "Title3", "Title4", "Title5", "Title6", "Title7"};
//    Long[] nors = {Long.valueOf(5), Long.valueOf(5), Long.valueOf(5), Long.valueOf(5),
//                   Long.valueOf(5),Long.valueOf(5),Long.valueOf(5)};
//    String[] dovs = {"2020-02-10", "2020-02-11", "2020-02-12", "2020-02-13",
//                     "2020-02-14", "2020-02-15", "2020-02-16"};
//    String[] tovs = {"11:10:11", "11:10:12", "11:10:13", "11:10:14",
//                     "11:10:15", "11:10:16", "11:10:17"};
//    String[] dods = {"2020-02-09", "2020-02-08","2020-02-08","2020-02-09",
//                     "2020-02-08", "2020-02-08", "2020-02-08"};
//    String[] regions = {"중구", "서구", "남구", "수성구",
//                        "수성구", "수성구", "수성구"};
//    String[] contents = {"GOOD", "GOOD","GOOD","GOOD", "GOOD", "GOOD", "GOOD"};
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
//    }
//
//    protected void saveAndCheck(String header, String title, String content, String region,
//                                String dod, String tov, String dov,
//                                Long nor){
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
//        // Activity 저장 됬는지 확인하기
//        assertThat(activity.getNor()).isEqualTo(nor);
//        assertThat(activity.getNoa()).isEqualTo(Long.valueOf(0));
//        assertThat(activity.getContent()).isEqualTo(content);
//        assertThat(activity.getRegion().getRegionId()).isEqualTo(regionModel.getRegionId());
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
//    @Test
//    public void 현재_공고글이_0개일_때_Count(){
//       Long nums = noticeActivityService.findNumsOfNotices();
//       System.out.println("nums: " + nums);
//       assertThat(nums).isEqualTo(Long.valueOf(0));
//    }
//    @Test
//    public void 현재_공고글이_4개일_때_Count(){
//        for(int i =0; i < 4; i++){
//            saveAndCheck("Bearer " + YAT, titles[i], contents[i], regions[i],
//                         dods[i], tovs[i], dovs[i], nors[i]);
//        }
//        Long nums = noticeActivityService.findNumsOfNotices();
//        System.out.println("nums: " + nums);
//        assertThat(nums).isEqualTo(Long.valueOf(4));
//    }
//    @Test
//    public void 현재_공고글이_1개_긴급공고글이_3개일때_Count(){
//        saveAndCheck("Bearer " + YAT, titles[0], contents[0], regions[0],
//                     dods[0], tovs[0], dovs[0], nors[0]);
//        Notice notice = noticeService.findByNoticeTitle(titles[0]);
//        Long noticeId = notice.getNoticeId();
//
//        urgentSaveAndCheck("Bearer " + YAT, noticeId, "긴급 공고입니다1.");
//        urgentSaveAndCheck("Bearer " + YAT, noticeId, "긴급 공고입니다2.");
//        urgentSaveAndCheck("Bearer " + YAT, noticeId, "긴급 공고입니다3.");
//
//        Long nums = noticeActivityService.findNumsOfNotices();
//        System.out.println("nums: " + nums);
//        assertThat(nums).isEqualTo(Long.valueOf(4));
//    }
//
//    @Test
//    public void 정상적으로_해당_공고글_조회(){
//        for(int i =0; i < 4; i++){
//            saveAndCheck("Bearer " + YAT, titles[i], contents[i], regions[i],
//                         dods[i], tovs[i], dovs[i], nors[i]);
//        }
//        Notice notice = noticeService.findByNoticeTitle(titles[0]);
//        Long noticeId = notice.getNoticeId();
//        Activity activity = noticeService.findActivityNoticeId(noticeId);
//
//        NoticeOneResponseDto noticeOneResponseDto =
//                noticeActivityService.getOneNoticeById(noticeId);
//
//        assertThat(noticeOneResponseDto.getId()).isEqualTo(notice.getNoticeId());
//        assertThat(noticeOneResponseDto.getTitle()).isEqualTo(notice.getTitle());
//        assertThat(noticeOneResponseDto.getContent()).isEqualTo(activity.getContent());
//        assertThat(noticeOneResponseDto.getNor()).isEqualTo(activity.getNor());
//        assertThat(noticeOneResponseDto.getNoa()).isEqualTo(activity.getNoa());
//
//        String dtovStr = noticeOneResponseDto.getDov() + " " + noticeOneResponseDto.getTov();
//        String dtodStr = noticeOneResponseDto.getDod() + " " + "23:59:59";
//
//        assertThat(dtovStr).isEqualTo(TimeConverter.LocalDateTimeToString(activity.getDtov()));
//        assertThat(dtodStr).isEqualTo(TimeConverter.LocalDateTimeToString(activity.getDtod()));
//    }
//    @Test(expected = NoticeNullException.class)
//    public void 해당하는_공고글이_없는_조회(){
//        NoticeOneResponseDto noticeOneResponseDto =
//                noticeActivityService.getOneNoticeById(Long.valueOf(123));
//    }
//    @Test(expected = NoticeNullException.class)
//    public void 해당하는_봉사활동이_없는_공고글_조회() {
//        saveAndCheck("Bearer " + YAT, titles[0], contents[0], regions[0],
//                     dods[0], tovs[0], dovs[0], nors[0]);
//        Notice notice = noticeService.findByNoticeTitle(titles[0]);
//        Long noticeId = notice.getNoticeId();
//
//        Activity activity = notice.getActivity();
//        Long activityId = activity.getActivityId();
//        activityService.deleteOnlyActivityById(activityId);
//
//        System.out.println("noticeId : " + noticeId);
//        NoticeOneResponseDto noticeOneResponseDto =
//                noticeActivityService.getOneNoticeById(noticeId);
//    }
//     // Paging
//    @Test
//    public void 정상적인_공고글들_페이징_조회() throws InterruptedException {
//        // 최근에 쓴 글 순서로 나오는가?
//
//        // 7개 저장
//        for(int i =0; i < 7; i++){
//            saveAndCheck("Bearer " + YAT, titles[i], contents[i], regions[i],
//                         dods[i], tovs[i], dovs[i], nors[i]);
//            Thread.sleep(1000);
//        }
//
//        Collection<NoticeResponseDto> noticeResponses = noticeActivityService.findNoticesByPage(0);
//        // 6개 여야함
//        assertThat(noticeResponses.size()).isEqualTo(6);
//
//        // Tile7 -> Tile 2로 가야함
//        Iterator<NoticeResponseDto> itr = noticeResponses.iterator();
//        int i = 7;
//        while(itr.hasNext()){
//            NoticeResponseDto noticeResponseDto = itr.next();
//            assertThat(noticeResponseDto.getTitle()).isEqualTo(titles[--i]);
//        }
//        System.out.println(noticeResponses);
//    }
//    @Test
//    public void 공고글이_6개가_안되는_조회() throws InterruptedException {
//        for(int i =0; i < 3; i++){
//            saveAndCheck("Bearer " + YAT, titles[i], contents[i], regions[i],
//                         dods[i], tovs[i], dovs[i], nors[i]);
//            Thread.sleep(1000);
//        }
//        Collection<NoticeResponseDto> noticeResponses = noticeActivityService.findNoticesByPage(0);
//        // 3개 여야함
//        assertThat(noticeResponses.size()).isEqualTo(3);
//
//        Iterator<NoticeResponseDto> itr = noticeResponses.iterator();
//        int i = 3;
//        while(itr.hasNext()){
//            NoticeResponseDto noticeResponseDto = itr.next();
//            assertThat(noticeResponseDto.getTitle()).isEqualTo(titles[--i]);
//        }
//        System.out.println(noticeResponses);
//    }
//
//    @Test(expected = NoticeNullException.class)
//    public void 요청하는_페이지가_너무_클_때() throws InterruptedException {
//        for(int i =0; i < 3; i++){
//            saveAndCheck("Bearer " + YAT, titles[i], contents[i], regions[i],
//                         dods[i], tovs[i], dovs[i], nors[i]);
//            Thread.sleep(1000);
//        }
//        Collection<NoticeResponseDto> noticeResponses = noticeActivityService.findNoticesByPage(4);
//    }
//
//
//}
