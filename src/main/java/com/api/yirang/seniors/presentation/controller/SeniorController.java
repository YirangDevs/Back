package com.api.yirang.seniors.presentation.controller;

import com.api.yirang.auth.domain.jwt.components.JwtParser;
import com.api.yirang.auth.support.utils.ParsingHelper;
import com.api.yirang.common.exceptions.ApiException;
import com.api.yirang.common.exceptions.Dto.ErrorDto;
import com.api.yirang.seniors.application.advancedService.SeniorVolunteerAdvancedService;
import com.api.yirang.seniors.presentation.dto.request.RegisterSeniorRequestDto;
import com.api.yirang.seniors.presentation.dto.response.SeniorResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
    // 엑셀로 피봉사자들을 업로드하는 API
    @PostMapping(value = "/total", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Map> registerSeniors(@RequestBody @NotEmpty Map<String, Collection> registerSeniors){
        Collection<RegisterSeniorRequestDto> registerSeniorRequestDtos = registerSeniors.get("seniors");
        System.out.println("[SeniorController] 피봉사자를 한 번에 추가하는 API 요청 받았습니다: " + registerSeniorRequestDtos);

        // 실패 리스트 저장
        Map<String, Map> res = new HashMap<>();
        Map<RegisterSeniorRequestDto, ErrorDto> mapInput = new HashMap<>();
        Iterator<RegisterSeniorRequestDto> itr = registerSeniorRequestDtos.iterator();
        while(itr.hasNext()){
            RegisterSeniorRequestDto registerSeniorRequestDto = itr.next();
            try {
                seniorVolunteerAdvancedService.registerSenior(registerSeniorRequestDto);
            } catch (ApiException e){
                mapInput.put(registerSeniorRequestDto, e.buildErrorDto());
            }
        }
        if (mapInput.size() == 0){
            res.put("failed_dtos", null);
        }
        else{
            res.put("failed_dtos", mapInput);
        }
        return res;
    }
    // 해당 지역 관련 피봉사자 GET API
    // 지역에 해당하는 히스토리를 줘야함
    @GetMapping(value = "/area", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Collection<SeniorResponseDto>> getSpecificRegionSeniors(@RequestParam("region") String region){
        System.out.println("[SeniorController] 해당 지역의 피봉사자 리스트를 원하는 API 요청 받았습니다: ");
        Map<String, Collection<SeniorResponseDto>> res = new HashMap<>();
        res.put("seniors", seniorVolunteerAdvancedService.findSeniorsByRegion(region));
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
    // 나중에 매칭 되지않은 봉사자와 매칭된 봉사자 case로 나누어야 할 듯
    // 피봉사자 데이터 DELETE API
    @DeleteMapping(value = "/{volunteerService_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVolunteerService(@PathVariable("volunteerService_id") Long volunteerServiceId){
        System.out.println("[SeniorController] 피봉사자의 정보 삭제를 원하는 API 요청 받았습니다: ");
        seniorVolunteerAdvancedService.deleteVolunteerService(volunteerServiceId);
    }

}
