package com.api.yirang.notice.service;


import com.api.yirang.auth.application.basicService.AdminService;
import com.api.yirang.auth.application.intermediateService.UserService;
import com.api.yirang.auth.domain.jwt.components.JwtParser;
import com.api.yirang.auth.support.utils.ParsingHelper;
import com.api.yirang.common.domain.region.model.Region;
import com.api.yirang.common.service.RegionService;
import com.api.yirang.notices.application.advancedService.NoticeActivityService;
import com.api.yirang.notices.application.basicService.ActivityService;
import com.api.yirang.notices.application.basicService.NoticeService;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.notices.domain.notice.model.Notice;
import com.api.yirang.notices.presentation.dto.NoticeRegisterRequestDto;
import com.api.yirang.notices.presentation.dto.embeded.ActivityRegisterRequestDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:properties/application-test.properties")
public class NoticeActivityCreateTest {

    // Test 하고자 하는 service
    @Autowired
    NoticeActivityService noticeActivityService;

    // 보조
    @Autowired
    NoticeService noticeService;
    @Autowired
    ActivityService activityService;
    @Autowired
    RegionService regionService;
    @Autowired
    AdminService adminService;
    @Autowired
    UserService userService;
    @Autowired
    JwtParser jwtParser;

    // Test variables
    String[] titles = {"Title1", "Title2", "Title3", "Title4"};
    Long[] nors = {Long.valueOf(5), Long.valueOf(5), Long.valueOf(5), Long.valueOf(5)};
    String[] dovs = {"2020-02-10", "2020-02-11", "2020-02-12", "2020-02-13"};
    String[] tovs = {"11:10:11", "11:10:12", "11:10:13", "11:10:14"};
    String[] dods = {"2020-02-09", "2020-02-08","2020-02-08","2020-02-08"};
    String[] regions = {"중구", "서구", "남구", "수성구"};
    String[] contents = {"GOOD", "GOOD","GOOD","GOOD"};

    Collection<Long> notices = new HashSet<>();
    Collection<Long> activities = new HashSet<>();

    String YAT = "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6IuycoOygleuvvCIsImltZ1VybCI6Imh0dHA6Ly9rLmtha2FvY2RuLm5ldC9kbi9oQTFWdS9idHFGS1h4ZlFzWC95MGhIalpsNlhaVWVScFdPa3lrTGtLL2ltZ182NDB4NjQwLmpwZyIsInVzZXJJZCI6IjE0Njg0MTYxMzkiLCJyb2xlIjoiVk9MVU5URUVSIiwiaWF0IjoxNjA0MTk5NjAzLCJleHAiOjE2MDQyODYwMDN9.bSwrE0i3nK9e6qCMzQpeamjPv2D95WTmMfUncW48ZrQ";

    @Before
    public void setUp(){
        System.out.println("테스트가 시작합니다.");
        System.out.println("YAT: " + YAT);
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(YAT));
        if (!adminService.isExistedByUserId(userId) ){
            userService.registerAdmin(userId);
        }
    }

    @After
    public void tearDown(){
        System.out.println("테스트가 끝났습니다.");
        // Notice 지우기
        Iterator<Long> noticeItr = notices.iterator();
        while(noticeItr.hasNext()){
            Long noticeId = noticeItr.next();
            noticeService.deleteNoticeByNoticeId(noticeId);
        }
        Iterator<Long> activityItr = activities.iterator();
        while(activityItr.hasNext()){
            Long activityId = activityItr.next();
            activityService.deleteOnlyActivityById(activityId);
        }
        notices.clear();
        activities.clear();
    }

    protected void save(String YAT, String title, String content, String region,
                        String dod, String tov, String dov,
                        Long nor){
        ActivityRegisterRequestDto activityRegisterRequestDto =
                ActivityRegisterRequestDto.builder()
                                          .content(content)
                                          .region(region)
                                          .dod(dod).tov(tov).dov(dov)
                                          .nor(nor)
                                          .build();

        NoticeRegisterRequestDto noticeRegisterRequestDto =
                NoticeRegisterRequestDto.builder()
                                        .title(title)
                                        .activityRegisterRequestDto(activityRegisterRequestDto)
                                        .build();
        System.out.println(noticeRegisterRequestDto);
        noticeActivityService.registerNew(YAT, noticeRegisterRequestDto);
    }


    @Test
    public void 정상적으로_하나_등록했을_떄(){
        ActivityRegisterRequestDto activityRegisterRequestDto =
                ActivityRegisterRequestDto.builder()
                                          .content(contents[0])
                                          .region(regions[0])
                                          .dod(dods[0]).tov(tovs[0]).dov(dovs[0])
                                          .nor(nors[0])
                                          .build();

        NoticeRegisterRequestDto noticeRegisterRequestDto =
                NoticeRegisterRequestDto.builder()
                                        .title(titles[0])
                                        .activityRegisterRequestDto(activityRegisterRequestDto)
                                        .build();
        System.out.println(noticeRegisterRequestDto);
        // 새로 등록하기
        noticeActivityService.registerNew(YAT, noticeRegisterRequestDto);

        Region region = regionService.findRegionByRegionName(regions[0]);
        Activity activity = activityService.findActivityByRegionAndDTOV(region, dovs[0], tovs[0]);
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(YAT));
        Notice notice = noticeService.findByNoticeTitle(titles[0]);
        notices.add(notice.getNoticeId());
        activities.add(activity.getActivityId());

        // Activity 저장 됬는지 확인하기
        assertThat(activity.getNor()).isEqualTo(nors[0]);
        assertThat(activity.getNoa()).isEqualTo(Long.valueOf(0));
        assertThat(activity.getContent()).isEqualTo(contents[0]);
        assertThat(activity.getRegion().getRegionId()).isEqualTo(region.getRegionId());

        // Notice 저장 됬는 지 확인하기
        assertThat(notice.getTitle()).isEqualTo(titles[0]);
        assertThat(notice.getAdmin().getAdminNumber()).isEqualTo(adminService.findAdminByUserId(userId).getAdminNumber());
        assertThat(notice.getActivity().getActivityId()).isEqualTo(activity.getActivityId());
    }
    @Test
    public void 여러개_저장할_떄(){
        // 저장
        for(int i =0; i < 4; i++){
            save(YAT, titles[i], contents[i], regions[i],
                 dods[i], tovs[i], dovs[i], nors[i]);
        }
        // 확인
        for(int i=0; i < 4; i++){
            Region region = regionService.findRegionByRegionName(regions[i]);
            Activity activity = activityService.findActivityByRegionAndDTOV(region, dovs[i], tovs[i]);
            Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(YAT));
            Notice notice = noticeService.findByNoticeTitle(titles[i]);
            notices.add(notice.getNoticeId());
            activities.add(activity.getActivityId());

            System.out.println(notice);
            System.out.println(activity);

            // Activity 저장 됬는지 확인하기
            assertThat(activity.getNor()).isEqualTo(nors[i]);
            assertThat(activity.getNoa()).isEqualTo(Long.valueOf(0));
            assertThat(activity.getContent()).isEqualTo(contents[i]);
            assertThat(activity.getRegion().getRegionId()).isEqualTo(region.getRegionId());

            // Notice 저장 됬는 지 확인하기
            assertThat(notice.getTitle()).isEqualTo(titles[i]);
            assertThat(notice.getAdmin().getAdminNumber()).isEqualTo(adminService.findAdminByUserId(userId).getAdminNumber());
            assertThat(notice.getActivity().getActivityId()).isEqualTo(activity.getActivityId());
        }
    }
    @Test
    public void 중복된_제목의_공고가_저장될_때(){

    }
    @Test
    public void 중복된_봉사활동이_저장될_때(){

    }
    @Test
    public void
}



