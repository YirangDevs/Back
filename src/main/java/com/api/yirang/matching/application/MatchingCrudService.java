package com.api.yirang.matching.application;

import com.api.yirang.matching.repository.maria.MatchingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MatchingCrudService {

    private final MatchingRepository matchingRepository;


}
