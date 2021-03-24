package com.api.yirang.email.application;

import com.api.yirang.auth.application.intermediateService.UserService;
import com.api.yirang.email.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailCrudService {

    private final UserService userService;
    private final EmailRepository emailRepository;


}
