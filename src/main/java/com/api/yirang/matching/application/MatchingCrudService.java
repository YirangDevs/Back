package com.api.yirang.matching.application;

import com.api.yirang.matching.exception.MatchingListEmptyException;
import com.api.yirang.matching.model.maria.Matching;
import com.api.yirang.matching.repository.maria.MatchingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MatchingCrudService {

    private final MatchingRepository matchingRepository;


    public List<Matching> findMatchingOnProcessByUserId(Long userId){
        List<Matching> matchingList = matchingRepository.findMatchingsByVolunteer_User_UserIdAndActivity_DtovAfterNow(userId, LocalDateTime.now());
        if (matchingList.isEmpty()){
            throw new MatchingListEmptyException();
        }
        return matchingList;
    }

}
