package com.api.yirang.auth.application.basicService;


import com.api.yirang.auth.domain.user.exceptions.AdminNullException;
import com.api.yirang.auth.domain.user.model.Admin;
import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.auth.repository.persistence.maria.AdminDao;
import com.api.yirang.common.domain.region.model.DistributionRegion;
import com.api.yirang.common.service.DistributionRegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    // DI Dao
    private final AdminDao adminDao;

    @Transactional
    public void save(User user){
        adminDao.save(
          Admin.builder()
               .user(user)
               .build()
        );
    }
    @Transactional
    public void delete(User user){
        adminDao.deleteByUser(user);

    }


    @Transactional
    public Admin findAdminByUserId(Long userId) {
        return adminDao.findAdminByUserId(userId).orElseThrow(AdminNullException::new);
    }

    @Transactional
    public boolean isExistedUser(User user) {
        return adminDao.existsAdminByUser(user);
    }

    @Transactional
    public boolean isExistedByUserId(Long userId){
        return adminDao.existsAdminByUser_UserId(userId);
    }

}
