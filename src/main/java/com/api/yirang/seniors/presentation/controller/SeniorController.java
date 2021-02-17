package com.api.yirang.seniors.presentation.controller;

import com.api.yirang.auth.application.basicService.AdminService;
import com.api.yirang.auth.domain.jwt.components.JwtParser;
import com.api.yirang.auth.support.type.Authority;
import com.api.yirang.auth.support.utils.ParsingHelper;
import com.api.yirang.common.exceptions.ApiException;
import com.api.yirang.common.exceptions.Dto.ErrorDto;
import com.api.yirang.common.support.custom.ValidCollection;
import com.api.yirang.common.support.type.Region;
import com.api.yirang.seniors.application.advancedService.SeniorVolunteerAdvancedService;
import com.api.yirang.seniors.presentation.dto.request.RegisterSeniorRequestDto;
import com.api.yirang.seniors.presentation.dto.request.RegisterTotalSeniorRequestDto;
import com.api.yirang.seniors.presentation.dto.response.SeniorResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/apis/seniors")
public class SeniorController {

    // DI Serivce
    private final SeniorVolunteerAdvancedService seniorVolunteerAdvancedService;

    // DI Jwt
    private final JwtParser jwtParser;

    /** Post **/
    // 낱개씩 추가하는 API
    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerSeniorOneByOne(@RequestBody @Valid RegisterSeniorRequestDto registerSeniorRequestDto){
        System.out.println("[SeniorController] 피봉사자를 낱개씩 추가하는 API 요청 받았습니다: " + registerSeniorRequestDto);
        seniorVolunteerAdvancedService.registerSenior(registerSeniorRequestDto);
    }

    // 엑셀 파일들을 검사하는 API
    @PostMapping(value = "/check", consumes = "application/json")
    public ResponseEntity checkSeniors(@RequestHeader("Authorization") String header,
                                       @RequestBody @NotEmpty @Valid ValidCollection<RegisterTotalSeniorRequestDto> registerTotalSeniorRequestDtos,
                                       BindingResult bindingResult){
        System.out.println("[SeniorController] 피봉사자 엑셀을 검사하는 API 요청 받았습니다: " + registerTotalSeniorRequestDtos);

        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
        Map<String, List<ErrorDto>> errorMap = new HashMap<>();
        List<ErrorDto> errorDtos = new ArrayList<>();

        // 빈 칸인지 체크
        if(registerTotalSeniorRequestDtos.size() == 0){
            errorDtos.add(
                    ErrorDto.builder()
                            .errorCode("100").errorName("RegisterSeniorRequests is Empty")
                            .build()
            );
        }
        // 다 형식이 올바른 지 체크
        else if(bindingResult.hasErrors()){
            bindingResult.getFieldErrors().forEach(e -> {
                errorDtos.add(
                        ErrorDto.builder()
                                .errorCode("111").errorName(e.getField())
                                .build());
            });
        }
        // 같은 날짜, 지역 인지 체크
        else if (!seniorVolunteerAdvancedService.checkSameDateAndSameRegion(registerTotalSeniorRequestDtos)){
            errorDtos.add( ErrorDto.builder()
                                   .errorCode("099").errorName("Does not have Same data and region")
                                   .build());
        }
        // 관리자의 관할 구역 인지 체크
        else if (!seniorVolunteerAdvancedService.checkMyArea(userId, registerTotalSeniorRequestDtos)){
            errorDtos.add( ErrorDto.builder()
                                   .errorCode("119").errorName("Not my Area")
                                   .build());
        }

        // 준 데이터 중에 중복되는 데이터들이 없는 지 체크
        else if (!seniorVolunteerAdvancedService.checkNotDuplicateAmongRequest(registerTotalSeniorRequestDtos)){
            errorDtos.add ( ErrorDto.builder()
                                    .errorCode("112").errorName("Have duplicated data in requests")
                                    .build());
        }
//        // 준 데이터 중에 기존의 것과 중복되는 거 없는 지 체크
        else {
            int lineNumber = 0;
            Iterator<RegisterTotalSeniorRequestDto> itr = registerTotalSeniorRequestDtos.iterator();
            while (itr.hasNext()) {
                RegisterTotalSeniorRequestDto registerTotalSeniorRequestDto = itr.next();
                if (!seniorVolunteerAdvancedService.checkNotDuplicateAmongExistedData(registerTotalSeniorRequestDto)) {
                    errorDtos.add(ErrorDto.builder()
                                          .errorCode("113")
                                          .errorName("[" + lineNumber + "]" + " Have duplicated data in existed data")
                                          .build());
                }
                lineNumber++;
            }
        }
        errorMap.put("Errors", errorDtos);

        return errorDtos.size() == 0 ? new ResponseEntity(HttpStatus.OK) : new ResponseEntity(errorMap, HttpStatus.BAD_REQUEST);
    }

    // 엑셀 파일을 저장하는 API
    @PostMapping(value = "/total", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerSeniors(@RequestBody @NotEmpty @Valid ValidCollection<RegisterTotalSeniorRequestDto> registerTotalSeniorRequestDtos){
        System.out.println("[SeniorController] 피봉사자 엑셀을 저장하는 API 요청 받았습니다: " + registerTotalSeniorRequestDtos);

        Iterator<RegisterTotalSeniorRequestDto> itr= registerTotalSeniorRequestDtos.iterator();
        while(itr.hasNext()){
            RegisterSeniorRequestDto registerSeniorRequestDto = new RegisterSeniorRequestDto(itr.next());
            seniorVolunteerAdvancedService.registerSenior(registerSeniorRequestDto);
        }
    }

    /** Get method **/
    // 해당 지역 관련 피봉사자 GET API
    // 지역에 해당하는 히스토리를 줘야함
    @GetMapping(value = "/area", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Collection<SeniorResponseDto>> getSpecificRegionSeniors(@RequestHeader("Authorization") String header,
                                                                               @RequestParam("region") Region region){
        System.out.println("[SeniorController] 해당 지역의 피봉사자 리스트를 원하는 API 요청 받았습니다: ");

        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
        Map<String, Collection<SeniorResponseDto>> res = new HashMap<>();
        res.put("seniors", seniorVolunteerAdvancedService.findSeniorsByRegion(region, userId));
        return res;
    }
    // 관리자 관할 구역 피봉사자 GET API
    @GetMapping(value = "/myArea", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Collection<SeniorResponseDto>> getMyRegionSeniors(@RequestHeader("Authorization") String header){
        System.out.println("[SeniorController] 자신 관할 구역의 피봉사자 리스트를 원하는 API 요청 받았습니다: ");
        Long userId = jwtParser.getUserIdFromJwt(ParsingHelper.parseHeader(header));
        Map<String, Collection<SeniorResponseDto> > res = new HashMap<>();
        res.put("seniors", seniorVolunteerAdvancedService.findSeniorsByMyArea(userId) );
        return res;
    }
    /** UPDATE **/
    // 피봉사자 데이터 UPDATE API
    @PutMapping(value = "/{volunteerService_id}", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void updateVolunteerService(@PathVariable("volunteerService_id") Long volunteerServiceId,
                             @RequestBody @Valid RegisterSeniorRequestDto registerSeniorRequestDto){
        System.out.println("[SeniorController] 피봉사자의 정보를 업데이트 하기를 원하는 API 요청 받았습니다: ");
        seniorVolunteerAdvancedService.updateVolunteerService(volunteerServiceId, registerSeniorRequestDto);
    }

    /** DELETE **/
    // 나중에 매칭 되지 않은 봉사자와 매칭된 봉사자 case로 나누어야 할 듯
    // 피봉사자 데이터 DELETE API
    @DeleteMapping(value = "/{volunteerService_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVolunteerService(@PathVariable("volunteerService_id") Long volunteerServiceId){
        System.out.println("[SeniorController] 피봉사자의 정보 삭제를 원하는 API 요청 받았습니다: ");
        seniorVolunteerAdvancedService.deleteVolunteerService(volunteerServiceId);
    }

}
