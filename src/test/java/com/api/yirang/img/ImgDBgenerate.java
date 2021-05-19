package com.api.yirang.img;


import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.auth.repository.persistence.maria.UserDao;
import com.api.yirang.img.model.maria.Img;
import com.api.yirang.img.repository.maria.ImgRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class ImgDBgenerate {

    @Autowired
    UserDao userDao;

    @Autowired
    ImgRepository imgRepository;



    @Test
    public void 이미지_추가하기(){
        List<User> users = userDao.findAll();

        users.stream().forEach(user -> imgRepository.save(
               Img.builder()
                  .user(user)
                  .build() ));
    }
}
