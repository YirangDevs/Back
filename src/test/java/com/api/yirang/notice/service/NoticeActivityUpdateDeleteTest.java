package com.api.yirang.notice.service;

import com.api.yirang.auth.application.basicService.AdminService;
import com.api.yirang.auth.application.intermediateService.UserService;
import com.api.yirang.auth.domain.jwt.components.JwtParser;
import com.api.yirang.auth.domain.user.exceptions.AdminNullException;
import com.api.yirang.auth.domain.user.model.Admin;
import com.api.yirang.auth.support.utils.ParsingHelper;
import com.api.yirang.common.domain.region.exception.RegionNullException;
import com.api.yirang.common.domain.region.model.Region;
import com.api.yirang.common.service.RegionService;
import com.api.yirang.common.support.time.TimeConverter;
import com.api.yirang.notices.application.advancedService.NoticeActivityService;
import com.api.yirang.notices.application.basicService.ActivityService;
import com.api.yirang.notices.application.basicService.NoticeService;
import com.api.yirang.notices.domain.activity.exception.ActivityNullException;
import com.api.yirang.notices.domain.notice.exception.LastExistedNotice;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.notices.domain.notice.exception.NoticeNullException;
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

    // revise variables
    String revisedTitle = "제목";
    String revisedContent = "바뀌어진 Content";
    String revisedRegion = "서구";
    String revisedDod = "2020-01-01";
    String revisedDov = "2020-01-01";
    String revisedTov = "11:11:11";
    Long revisedNor = Long.valueOf(55);

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

        NoticeRegisterRequestDto noticeRegisterRequestDto = NoticeRegisterRequestDto.builder()
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

    public void reviseAndCheck(String header, Long noticeId){
        Notice notice = noticeService.findByNoticeId(noticeId);
        // 수정할 requestDTO 만들기
        ActivityRegisterRequestDto activityRegisterRequestDto = ActivityRegisterRequestDto.builder()
                                                                                          .content(revisedContent)
                                                                                          .region(revisedRegion)
                                                                                          .dod(revisedDod).tov(revisedTov)
                                                                                          .dov(revisedDov).nor(revisedNor)
                                                                                          .build();

        NoticeRegisterRequestDto noticeRegisterRequestDto = NoticeRegisterRequestDto.builder()
                                                                                    .title(revisedTitle)
                                                                                    .activityRegisterRequestDto(activityRegisterRequestDto)
                                                                                    .build();

        noticeActivityService.updateOneNotice("Bearer " + YAT, noticeId, noticeRegisterRequestDto);


        // 값이 제대로 바뀌었는지
        String revisedDtovStr = revisedDov + " " + revisedTov;
        String revisedDtodStr = revisedDod + " " + "23:59:59";

        // 다시 값을 불러오기
        Notice revisedNotice = noticeService.findByNoticeId(noticeId);
        Activity activity = noticeService.findActivityNoticeId(noticeId);

        assertThat(revisedNotice.getTitle()).isEqualTo(revisedTitle);
        assertThat(activity.getContent()).isEqualTo(revisedContent);
        assertThat(activity.getNor()).isEqualTo(revisedNor);
        assertThat(activity.getNoa()).isEqualTo(Long.valueOf(0));
        assertThat(TimeConverter.LocalDateTimeToString(activity.getDtov())).isEqualTo(revisedDtovStr);
        assertThat(TimeConverter.LocalDateTimeToString(activity.getDtod())).isEqualTo(revisedDtodStr);
        assertThat(activity.getRegion().getRegionName()).isEqualTo(revisedRegion);
    }

    @Test
    public void 정상적인_공고글_수정(){
        saveAndCheck("Bearer " + YAT, titles[0], contents[0], regions[0],
                     dods[0], tovs[0], dovs[0], nors[0]);
        // noticeId 찾아내기
        Notice notice = noticeService.findByNoticeTitle(titles[0]);
        Long noticeId = notice.getNoticeId();
        reviseAndCheck("Bearer " + YAT, noticeId);

        // title로 다시 부르면 없어져야함
        assertFalse(noticeService.isExistedNoticeByNoticeTitle(titles[0]));
    }
    @Test(expected = AdminNullException.class)
    public void 공고글_수정시_관리자가_없을_때(){
        saveAndCheck("Bearer " + YAT, titles[0], contents[0], regions[0],
                     dods[0], tovs[0], dovs[0], nors[0]);
        // noticeId 찾아내기
        Notice notice = noticeService.findByNoticeTitle(titles[0]);
        Long noticeId = notice.getNoticeId();
        // admin 삭제
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader("Bearer " + YAT));
        userService.fireAdmin(userId);

        reviseAndCheck("Bearer " + YAT, noticeId);

    }
    @Test(expected = RegionNullException.class)
    public void 수정시_없는_지역을_선택하면(){
        saveAndCheck("Bearer " + YAT, titles[0], contents[0], regions[0],
                     dods[0], tovs[0], dovs[0], nors[0]);
        Notice notice = noticeService.findByNoticeTitle(titles[0]);
        Long noticeId = notice.getNoticeId();

        ActivityRegisterRequestDto activityRegisterRequestDto = ActivityRegisterRequestDto.builder()
                                                                                          .content(revisedContent)
                                                                                          .region("해운대구")
                                                                                          .dod(revisedDod).tov(revisedTov)
                                                                                          .dov(revisedDov).nor(revisedNor)
                                                                                          .build();

        NoticeRegisterRequestDto noticeRegisterRequestDto = NoticeRegisterRequestDto.builder()
                                                                                    .title(revisedTitle)
                                                                                    .activityRegisterRequestDto(activityRegisterRequestDto)
                                                                                    .build();

        noticeActivityService.updateOneNotice("Bearer " + YAT, noticeId, noticeRegisterRequestDto);
    }

    @Test(expected = NoticeNullException.class)
    public void 수정시_요청하는_공고글이_없을때(){
        reviseAndCheck("Bearer " + YAT, Long.valueOf(11));
    }
    @Test(expected = NoticeNullException.class)
    public void 수정시_요청하는_봉사가_없을때(){
        saveAndCheck("Bearer " + YAT, titles[0], contents[0], regions[0],
                     dods[0], tovs[0], dovs[0], nors[0]);
        Notice notice = noticeService.findByNoticeTitle(titles[0]);
        Long noticeId = notice.getNoticeId();

        // acitiy 삭제
        Activity activity = notice.getActivity();
        Long activityId = activity.getActivityId();
        activityService.deleteOnlyActivityById(activityId);
        activities.remove(activityId);

        reviseAndCheck("Bearer " + YAT, noticeId);
    }

    @Test
    public void 활동을_수정하면_다른_공고글의_봉사_내역도_수정되는_가(){
        saveAndCheck("Bearer " + YAT, titles[0], contents[0], regions[0],
                     dods[0], tovs[0], dovs[0], nors[0]);
        // noticeId 찾아내기
        Notice notice = noticeService.findByNoticeTitle(titles[0]);
        Long noticeId = notice.getNoticeId();

        String otherTitle = "긴급 공고입니다1.";
        String otherTitleSecond = "보고 드립니다.";
        urgentSaveAndCheck("Bearer " + YAT, noticeId, otherTitle);
        urgentSaveAndCheck("Bearer " + YAT, noticeId, otherTitleSecond);

        reviseAndCheck("Bearer " + YAT, noticeId);

        Notice otherNotice = noticeService.findByNoticeTitle(otherTitle);
        Activity activity = otherNotice.getActivity();

        // 다른 긴급 공고 1
        String revisedDtovStr = revisedDov + " " + revisedTov;
        String revisedDtodStr = revisedDod + " " + "23:59:59";

        assertThat(activity.getContent()).isEqualTo(revisedContent);
        assertThat(activity.getNor()).isEqualTo(revisedNor);
        assertThat(activity.getNoa()).isEqualTo(Long.valueOf(0));
        assertThat(TimeConverter.LocalDateTimeToString(activity.getDtov())).isEqualTo(revisedDtovStr);
        assertThat(TimeConverter.LocalDateTimeToString(activity.getDtod())).isEqualTo(revisedDtodStr);
        assertThat(activity.getRegion().getRegionName()).isEqualTo(revisedRegion);

        // 다른 긴급 공고 2
        Notice otherNoticeSecond = noticeService.findByNoticeTitle(otherTitleSecond);
        Activity activitySecond = otherNoticeSecond.getActivity();

        assertThat(activitySecond.getContent()).isEqualTo(revisedContent);
        assertThat(activitySecond.getNor()).isEqualTo(revisedNor);
        assertThat(activitySecond.getNoa()).isEqualTo(Long.valueOf(0));
        assertThat(TimeConverter.LocalDateTimeToString(activitySecond.getDtov())).isEqualTo(revisedDtovStr);
        assertThat(TimeConverter.LocalDateTimeToString(activitySecond.getDtod())).isEqualTo(revisedDtodStr);
        assertThat(activitySecond.getRegion().getRegionName()).isEqualTo(revisedRegion);
    }

    // 기본 가정은 각 활동은 최소한 하나 이상의 공고글을 가지고 있어야 한다.
    // 자신의 활동과 관련된 모든 공고가 없어질 경우 에는 활동도 삭제한다.
    @Test
    public void 정상적인_공고글_삭제(){
        // 다른 글은 괜찮아야함
        saveAndCheck("Bearer " + YAT, titles[0], contents[0], regions[0],
                     dods[0], tovs[0], dovs[0], nors[0]);
        // noticeId 찾아내기
        Notice notice = noticeService.findByNoticeTitle(titles[0]);
        Long noticeId = notice.getNoticeId();

        Activity activity = noticeService.findActivityNoticeId(noticeId);

        String otherTitle = "긴급 공고입니다1.";
        String otherTitleSecond = "보고 드립니다.";
        urgentSaveAndCheck("Bearer " + YAT, noticeId, otherTitle);
        urgentSaveAndCheck("Bearer " + YAT, noticeId, otherTitleSecond);

        noticeActivityService.deleteOneNotice(noticeId);

        Notice notice1 = noticeService.findByNoticeTitle(otherTitle);
        Notice notice2 = noticeService.findByNoticeTitle(otherTitleSecond);

        Activity activity1 = noticeService.findActivityNoticeId(notice1.getNoticeId());
        Activity activity2 = noticeService.findActivityNoticeId(notice2.getNoticeId());

        // 존재하고
        assertTrue(activityService.isExistedActivityById(activity.getActivityId()));

        // 다른 공고글의 activity와 같다.
        assertThat(activity.getActivityId()).isEqualTo(activity1.getActivityId());
        assertThat(activity.getActivityId()).isEqualTo(activity2.getActivityId());

    }
    @Test(expected = ActivityNullException.class)
    public void 요청하는_공고글이_없는_공고글_삭제(){
        noticeActivityService.deleteOneNotice(Long.valueOf(100));
    }
    @Test(expected = LastExistedNotice.class)
    public void 삭제시_이_공고글이_연관된_마지막_공고글일_경우(){
        saveAndCheck("Bearer " + YAT, titles[0], contents[0], regions[0],
                     dods[0], tovs[0], dovs[0], nors[0]);
        // noticeId 찾아내기
        Notice notice = noticeService.findByNoticeTitle(titles[0]);
        Long noticeId = notice.getNoticeId();

        noticeActivityService.deleteOneNotice(noticeId);
    }
    @Test
    public void 강력_삭제(){
        saveAndCheck("Bearer " + YAT, titles[0], contents[0], regions[0],
                     dods[0], tovs[0], dovs[0], nors[0]);
        // noticeId 찾아내기
        Notice notice = noticeService.findByNoticeTitle(titles[0]);
        Long noticeId = notice.getNoticeId();

        noticeActivityService.deleteOneNoticeWithForce(noticeId);
    }
}
