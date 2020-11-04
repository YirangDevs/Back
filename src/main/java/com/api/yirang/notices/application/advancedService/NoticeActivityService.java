package com.api.yirang.notices.application.advancedService;

import com.api.yirang.auth.application.basicService.AdminService;
import com.api.yirang.auth.domain.jwt.components.JwtParser;
import com.api.yirang.auth.domain.user.model.Admin;
import com.api.yirang.auth.support.utils.ParsingHelper;
import com.api.yirang.common.domain.region.model.Region;
import com.api.yirang.common.service.RegionService;
import com.api.yirang.notices.application.basicService.ActivityService;
import com.api.yirang.notices.application.basicService.NoticeService;
import com.api.yirang.notices.domain.activity.converter.ActivityConverter;
import com.api.yirang.notices.domain.notice.exception.LastExistedNotice;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.notices.domain.notice.converter.NoticeConverter;
import com.api.yirang.notices.domain.notice.model.Notice;
import com.api.yirang.notices.presentation.dto.NoticeOneResponseDto;
import com.api.yirang.notices.presentation.dto.NoticeRegisterRequestDto;
import com.api.yirang.notices.presentation.dto.NoticeResponseDto;
import com.api.yirang.notices.presentation.dto.embeded.ActivityRegisterRequestDto;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeActivityService {

    // DI service
    private final NoticeService noticeService;
    private final ActivityService activityService;

    // DI user Service
    private final AdminService adminService;
    private final RegionService regionService;

    // DI JwtParser
    private final JwtParser jwtParser;

    public void registerNew(String header, NoticeRegisterRequestDto noticeRequestDto) {

        System.out.println("[NoticeActivityService]: registerNew를 실행하겠습니다.");

        // header 뜯어서 누구인지 판단하고, admin 구하기
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
        Admin admin = adminService.findAdminByUserId(userId);

        System.out.println("[NoticeActivityService]: admin: " + admin);

        // Dto를 받아서 뜯어서 -> Notice랑 Activity로 나누어야 함
        ActivityRegisterRequestDto activityRegisterRequestDto = noticeRequestDto.getActivityRegisterRequestDto();


        // 지역 구하기
        Region region = regionService.findRegionByRegionName(activityRegisterRequestDto.getRegion());

        System.out.println("[NoticeActivityService]: 지역은: " + region);

        // Activity 받아서 DB에 저장
        Activity activity = ActivityConverter.ConvertFromDtoToModel(activityRegisterRequestDto, region);
        activityService.save(activity);

        System.out.println("[NoticeActivityService]: activity를 저장했습니다.");


        // Notice 받아서, DB에 저장
        String title = noticeRequestDto.getTitle();
        Notice notice = NoticeConverter.convertFromDtoToModel(title, admin, activity);
        noticeService.save(notice);

        System.out.println("[NoticeActivityService]: Notice를 저장했습니다. ");
    }

    public NoticeOneResponseDto getOneNoticeById(Long noticeId) {

        System.out.println("[NoticeActivityService]: getOneNoticeById를 실행하겠습니다.");
        // Notice Id로 Notice 불러오기
        Notice notice = noticeService.findByNoticeId(noticeId);

        System.out.println("[NoticeActivityService]: Notice: " + notice);

        // Notice에 해당하는 Activity 불러오기
        Activity activity = noticeService.findActivityNoticeId(noticeId);

        System.out.println("[NoticeActivityService]: activity: " + activity);

        // preprocessing 하고 Dto 만들기
        return NoticeConverter.convertFromNoticeToOneResponse(notice, activity);
    }


    public Collection<NoticeResponseDto> findNoticesByPage(Integer pageNum) {
        // 갯수는 고정적
        int elementNums = 6;
        // Pageable 만들기
        Pageable pageWithSixElements = PageRequest.of(pageNum, elementNums, Sort.by("dtow").descending());

        System.out.println("[NoticeActivityService]: findNoticesByPage를 실행하겠습니다.");

        // collections Notice 구하기
        Collection<Notice> notices = noticeService.findAllWithPage(pageWithSixElements);

        System.out.println("[NoticeActivityService]: Collection<Notice>: " + notices);

        // Notice -> NoticeResponseDTO로 바꾸기
        Collection<NoticeResponseDto> noticeResponseDtos = new ArrayList<>();

        Iterator<Notice> itr = notices.iterator();
        while(itr.hasNext()){
            Notice notice = itr.next();
            Activity activity = notice.getActivity();
            noticeResponseDtos.add(
                    NoticeConverter.convertFromNoticeToResponse(notice, activity));
        }

        System.out.println("[NoticeActivityService]: noticeResponseDtos를 만들어서 내보겠습니다.");

        return noticeResponseDtos;
    }

    public Long findNumsOfNotices() {

        System.out.println("[NoticeActivityService]: findNumsOfNotices를 실행하겠습니다.");
        return noticeService.countNumsOfNotices();
    }

    public void registerUrgent(String header, Long noticeId,
                               @NotBlank(message = "title is mandatory")
                               @Length(min=3, max= 100, message = "title should be between 5 ~ 100") String title) {

        System.out.println("[NoticeActivityService]: registerUrgent를 실행하겠습니다.");
        System.out.println("[NoticeActivityService]: title을 받았습니다. " + title);

        // header 뜯어서 작성한 admin 누구인지 알아냄
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
        Admin admin = adminService.findAdminByUserId(userId);
        // noticeId를 이용해서 Activity를 가져옴
        Activity activity = noticeService.findActivityNoticeId(noticeId);

        System.out.println("[NoticeActivityService]: Admin과 Activity을 구했습니다.");

        // 새로운 Notice를 만들고 저장
        Notice newUrgentNotice = NoticeConverter.convertFromDtoToModel(title, admin, activity);
        noticeService.save(newUrgentNotice);

        System.out.println("[NoticeActivityService]: notice를 저장했습니다.");
    }

    public void updateOneNotice(String header, Long noticeId,
                                NoticeRegisterRequestDto noticeRegisterRequestDto) {
        // header 뜯어서 작성한 admin 누구인지 알아내기
        System.out.println("[NoticeActivityService]: registerUrgent를 실행하겠습니다.");

        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
        Admin admin = adminService.findAdminByUserId(userId);

        // Request에서 둘이 나누기
        ActivityRegisterRequestDto activityRegisterRequestDto =
                noticeRegisterRequestDto.getActivityRegisterRequestDto();

        // Notice update 하기
        String newTitle = noticeRegisterRequestDto.getTitle();
        noticeService.update(noticeId, newTitle, admin);

        // Notice와 해당되는 Activity 들고 오기
        Activity activity = noticeService.findActivityNoticeId(noticeId);
        Region region = regionService.findRegionByRegionName(activityRegisterRequestDto.getRegion());

        // Activity update 하기
        Activity toBeUpdatedActivity = ActivityConverter.ConvertFromDtoToModel(activityRegisterRequestDto, region);
        activityService.update(activity.getActivityId(), toBeUpdatedActivity);
    }

    public void deleteOneNotice(Long noticeId) {
        //notice가 있는 지 확인
        // 이 글이 마지막 활동의 마지막 글인지 아닌지, count 해서
        Activity activity = noticeService.findActivityNoticeId(noticeId);
        Long relatedNoticeNums =  noticeService.countNumsOfNoticesByActivity(activity);
        if (relatedNoticeNums == 1){
            throw new LastExistedNotice();
        }
        noticeService.deleteNoticeByNoticeId(noticeId);
    }
    public void deleteOneNoticeWithForce(Long noticeId){
        Activity activity = noticeService.findActivityNoticeId(noticeId);
        activityService.deleteActivityById(activity.getActivityId());
        noticeService.deleteNoticeByNoticeId(noticeId);
    }
}
