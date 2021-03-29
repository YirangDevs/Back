package com.api.yirang.matching.application;

import com.api.yirang.matching.model.maria.Matching;
import com.api.yirang.matching.repository.maria.MatchingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MatchingCrudService {

    private final MatchingRepository matchingRepository;


    public void save(Matching matching){
        matchingRepository.save(matching);
    }

}
