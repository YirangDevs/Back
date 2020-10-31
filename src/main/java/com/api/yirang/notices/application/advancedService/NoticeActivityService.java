package com.api.yirang.notices.application.advancedService;

import com.api.yirang.auth.application.basicService.AdminService;
import com.api.yirang.auth.application.intermediateService.UserService;
import com.api.yirang.auth.domain.jwt.components.JwtParser;
import com.api.yirang.auth.domain.user.model.Admin;
import com.api.yirang.auth.support.utils.ParsingHelper;
import com.api.yirang.common.domain.region.model.Region;
import com.api.yirang.common.service.RegionService;
import com.api.yirang.notices.application.basicService.ActivityService;
import com.api.yirang.notices.application.basicService.NoticeService;
import com.api.yirang.notices.domain.activity.converter.ActivityConverter;
import com.api.yirang.notices.domain.activity.model.Activity;
import com.api.yirang.notices.domain.notice.converter.NoticeConverter;
import com.api.yirang.notices.domain.notice.model.Notice;
import com.api.yirang.notices.presentation.dto.NoticeOneResponseDto;
import com.api.yirang.notices.presentation.dto.NoticeRegisterRequestDto;
import com.api.yirang.notices.presentation.dto.NoticeResponseDto;
import com.api.yirang.notices.presentation.dto.NoticeResponsesDto;
import com.api.yirang.notices.presentation.dto.embeded.ActivityRegisterRequestDto;
import jdk.internal.jimage.ImageStrings;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

@Service
@RequiredArgsConstructor
public class NoticeActivityService {

    // DI service
    private final NoticeService noticeService;
    private final ActivityService activityService;

    // DI user Service
    private final AdminService adminService;
    private final RegionService regionService;

    // DI JwtParser
    private final JwtParser jwtParser;

    @Transactional
    public void registerNew(String header, NoticeRegisterRequestDto noticeRequestDto) {

        // header 뜯어서 누구인지 판단하고, admin 구하기
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
        Admin admin = adminService.findAdminByUserId(userId);

        // Dto를 받아서 뜯어서 -> Notice랑 Activity로 나누어야 함
        ActivityRegisterRequestDto activityRegisterRequestDto =
                noticeRequestDto.getActivityRegisterRequestDto();

        // 지역 구하기
        Region region = regionService.findRegionByRegionName(activityRegisterRequestDto.getRegion());

        // Activity 받아서 DataBase에 저장
        Activity activity = ActivityConverter.ConvertFromDtoToModel(activityRegisterRequestDto, region);
        activityService.save(activity);

        // Notice 구하기
        String title = noticeRequestDto.getTitle();
        Notice notice = NoticeConverter.convertFromDtoToModel(title, admin, activity);
        noticeService.save(notice);

    }

    @Transactional
    public NoticeOneResponseDto getOneNoticeById(Long noticeId) {
        // Notice Id로 Notice 불러오기
        Notice notice = noticeService.findByNoticeId(noticeId);

        // Notice에 해당하는 Activity 불러오기
        Activity activity = notice.getActivity();

        // preprocessing 하고 Dto 만들기
        return NoticeConverter.convertFromNoticeToOneResponse(notice, activity);
    }


    public NoticeResponsesDto findNoticesByPage(Integer pageNum) {
        // 갯수는 고정적
        int elementNums = 6;
        // Pageable 만들기
        Pageable pageWithSixElements = PageRequest.of(pageNum, elementNums, Sort.by("dtow").descending());

        // collections Notice 구하기
        Collection<Notice> notices = noticeService.findAllWithPage(pageWithSixElements);

        // Notice -> NoticeResponseDTO로 바꾸기
        Collection<NoticeResponseDto> noticeResponseDtos = new HashSet<>();

        Iterator<Notice> itr = notices.iterator();
        while(itr.hasNext()){
            Notice notice = itr.next();
            Activity activity = notice.getActivity();
            noticeResponseDtos.add(
                    NoticeConverter.convertFromNoticeToResponse(notice, activity));
        }

        return NoticeResponsesDto.builder()
                                 .notices(noticeResponseDtos)
                                 .build();
    }
}
