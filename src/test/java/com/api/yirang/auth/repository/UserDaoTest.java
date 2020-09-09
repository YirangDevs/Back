package com.api.yirang.auth.repository;


import com.api.yirang.auth.repository.persistence.maria.UserDao;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDaoTest {

    @Autowired
    UserDao userDao;

    @After
    public void cleanUp(){
        userDao.deleteAll();
    }

    @Test
    public void 유저데이터_저장_후_불러오기(){

    }
}
