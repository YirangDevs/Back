package com.api.yirang.notice.service;

import com.api.yirang.auth.application.basicService.AdminService;
import com.api.yirang.auth.application.intermediateService.UserService;
import com.api.yirang.auth.domain.jwt.components.JwtParser;
import com.api.yirang.auth.domain.user.model.Admin;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:properties/application-test.properties")
public class NoticeActivityUpdateDeleteTest {


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
    String[] titles = {"Title1", "Title2", "Title3", "Title4", "Title5", "Title6", "Title7"};
    Long[] nors = {Long.valueOf(5), Long.valueOf(5), Long.valueOf(5), Long.valueOf(5),
                   Long.valueOf(5),Long.valueOf(5),Long.valueOf(5)};
    String[] dovs = {"2020-02-10", "2020-02-11", "2020-02-12", "2020-02-13",
                     "2020-02-14", "2020-02-15", "2020-02-16"};
    String[] tovs = {"11:10:11", "11:10:12", "11:10:13", "11:10:14",
                     "11:10:15", "11:10:16", "11:10:17"};
    String[] dods = {"2020-02-09", "2020-02-08","2020-02-08","2020-02-09",
                     "2020-02-08", "2020-02-08", "2020-02-08"};
    String[] regions = {"중구", "서구", "남구", "수성구",
                        "수성구", "수성구", "수성구"};
    String[] contents = {"GOOD", "GOOD","GOOD","GOOD", "GOOD", "GOOD", "GOOD"};

    Collection<Long> notices = new HashSet<>();
    Collection<Long> activities = new HashSet<>();

    @Value("${test.api.token.yirang_access_token}")
    String YAT;

    @Before
    public void setUp(){
        System.out.println("테스트가 시작합니다.");
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader("Bearer " + YAT));
        if (!adminService.isExistedByUserId(userId) ){
            userService.registerAdmin(userId);
        }
        noticeService.deleteAll();
        activityService.deleteAll();
    }

    @After
    public void tearDown(){
        System.out.println("테스트가 끝났습니다.");
    }

    protected void saveAndCheck(String header, String title, String content, String region,
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
        noticeActivityService.registerNew(header, noticeRegisterRequestDto);

        Region regionModel = regionService.findRegionByRegionName(region);
        Activity activity = activityService.findActivityByRegionAndDTOV(regionModel, dov, tov);
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
        Notice notice = noticeService.findByNoticeTitle(title);
        notices.add(notice.getNoticeId());
        activities.add(activity.getActivityId());
        // Activity 저장 됬는지 확인하기
        assertThat(activity.getNor()).isEqualTo(nor);
        assertThat(activity.getNoa()).isEqualTo(Long.valueOf(0));
        assertThat(activity.getContent()).isEqualTo(content);
        assertThat(activity.getRegion().getRegionId()).isEqualTo(regionModel.getRegionId());
        // Notice 저장 됬는 지 확인하기
        assertThat(notice.getTitle()).isEqualTo(title);
        assertThat(notice.getAdmin().getAdminNumber()).isEqualTo(adminService.findAdminByUserId(userId).getAdminNumber());
        assertThat(notice.getActivity().getActivityId()).isEqualTo(activity.getActivityId());
    }
    public void urgentSaveAndCheck(String header, Long noticeId, String title){
        noticeActivityService.registerUrgent(header, noticeId, title);
        Notice notice = noticeService.findByNoticeTitle(title);
        // 나중에 삭제하기 위해서
        notices.add(notice.getNoticeId());

        // 작성자 확인
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
        Admin admin = notice.getAdmin();
        assertThat(admin.getAdminNumber()).isEqualTo(adminService.findAdminByUserId(userId).getAdminNumber());

        // Activity 그대로인지 확인
        Notice originNotice = noticeService.findByNoticeId(noticeId);
        Activity originActivity = originNotice.getActivity();

        Activity activity = notice.getActivity();
        assertThat(originActivity.getActivityId()).isEqualTo(activity.getActivityId());
    }

    @Test
    public void 정상적인_공고글_수정(){

    }
    @Test
    public void 공고글_수정_중_관리자가_없을_때(){

    }
    @Test
    public void 요청하는_공고글이_없을때(){

    }
    @Test
    public void 요청하는_봉사가_없을때(){

    }
    @Test
    public void 활동을_수정하면_다른_공고글도_수정되는_가(){

    }

    @Test
    public void 정상적인_공고글_삭제(){

    }
    @Test
    public void 요청하는_공고글이_없는_공고글_삭제(){

    }
    @Test
    public void 공고글만_삭제하는_경우(){
        // 다른 글은 괜찮아야함
    }
    @Test
    public void 이_공고글이_연관된_마지막_공고글일_경우(){

    }
}
