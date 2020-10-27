package com.api.yirang.auth.application.basicService;


import com.api.yirang.auth.domain.user.exceptions.AdminNullException;
import com.api.yirang.auth.domain.user.model.Admin;
import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.auth.repository.persistence.maria.AdminDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    // DI Dao
    private final AdminDao adminDao;

    public final void save(User user){
        adminDao.save(
          Admin.builder()
               .user(user)
               .build()
        );
    }
    public final Admin findAdminByUserId(Long userId) {
        return adminDao.findAdminByUserId(userId).orElseThrow(AdminNullException::new);
    }


    public final boolean isExistedUser(User user) {
        return adminDao.findAdminByUser(user);
    }

}
