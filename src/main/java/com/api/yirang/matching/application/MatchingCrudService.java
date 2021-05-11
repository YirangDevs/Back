package com.api.yirang.matching.application;

import com.api.yirang.matching.exception.MatchingListEmptyException;
import com.api.yirang.matching.model.maria.Matching;
import com.api.yirang.matching.exceptions.MatchingNullException;
import com.api.yirang.matching.exceptions.UnMatchingListNullException;
import com.api.yirang.matching.model.mongo.UnMatchingList;
import com.api.yirang.matching.repository.maria.MatchingRepository;
import com.api.yirang.matching.repository.mongo.UnMatchingListRepository;
import com.api.yirang.notices.domain.activity.model.Activity;
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
    private final UnMatchingListRepository unMatchingListRepository;


    public void save(Matching matching){
        matchingRepository.save(matching);
    }

    public void saveAll(List<Matching> matchingList){
        matchingRepository.saveAll(matchingList);
    }

    public void saveUniqueUnMatchingListRepository(UnMatchingList unMatchingList){
        unMatchingListRepository.deleteUnMatchingListByActivityId(unMatchingList.getActivityId());
        unMatchingListRepository.save(unMatchingList);
    }

    public List<Matching> findMatchingsByActivity(Activity activity, Boolean nullable) {
        List<Matching> matchingList = matchingRepository.findMatchingsByActivity(activity);
        if (matchingList.size() == 0 && !nullable){
            throw new MatchingNullException();
        }
        return matchingList;
    }

    public List<Matching> findMatchingsByUserId(Long userId){
        List<Matching> matchingList = matchingRepository.findMatchingsByVolunteer_User_UserId(userId);
        if (matchingList.size() == 0){
            throw new MatchingNullException();
        }
        return matchingList;
    }


    public UnMatchingList findUnmatchingByActivityId(Long activityId) {
        return unMatchingListRepository.findUnMatchingListByActivityId(activityId).orElseThrow(UnMatchingListNullException::new);
    }


    public List<Matching> findMatchingOnProcessByUserId(Long userId){
        List<Matching> matchingList = matchingRepository.findMatchingsByVolunteer_User_UserIdAndActivity_DtovAfterNow(userId, LocalDateTime.now());
        if (matchingList.isEmpty()){
            throw new MatchingListEmptyException();
        }
        return matchingList;
    }

    public void deleteAllWithActivity(Activity activity) {
        matchingRepository.deleteAllByActivity(activity);
    }
}
