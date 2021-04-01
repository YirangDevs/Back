package com.api.yirang.auth.application.basicService;


import com.api.yirang.auth.domain.user.exceptions.AdminNullException;
import com.api.yirang.auth.domain.user.exceptions.AlreadyExistedArea;
import com.api.yirang.auth.domain.user.exceptions.AreaNullException;
import com.api.yirang.auth.domain.user.exceptions.UserNullException;
import com.api.yirang.auth.domain.user.model.Admin;
import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.auth.repository.persistence.maria.AdminDao;
import com.api.yirang.common.support.converter.RegionListConverter;
import com.api.yirang.common.support.type.Region;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

    // DI Dao
    private final AdminDao adminDao;

    public Admin save(User user){
        return adminDao.save(
                Admin.builder()
                     .user(user)
                     .build()
            );
    }
    public void delete(User user){
        adminDao.deleteByUser(user);
    }

    public void updateRegionsByUserId(Long userId, List<Region> regions){
        findAdminByUserId(userId);
        adminDao.updateRegionsByUserId(userId,
                                       RegionListConverter.convertFromListToString(regions));
    }

    public void addAreaByUserId(Long userId, Region region){
        Admin admin = findAdminByUserId(userId);
        System.out.println(admin);
        List<Region> regions = admin.getRegions();
        // Add
        if ( regions.contains(region)){
            throw new AlreadyExistedArea();
        }
        regions.add(region);
        updateRegionsByUserId(userId, regions);

        // 저장 잘되었는지 확인
        System.out.println("저장 후:" + findAreasByUserId(userId));
    }

    public void deleteAreaByUserId(Long userId, Region region){
        Admin admin = findAdminByUserId(userId);
        List<Region> regions = admin.getRegions();
        if ( !regions.contains(region) ){
            throw new AreaNullException();
        }
        regions.remove(region);
        updateRegionsByUserId(userId, regions);
        // 저장 잘되었는지 확인
        System.out.println("저장 후:" + findAreasByUserId(userId));
    }

    public List<Region> findAreasByUserId(Long userId){
        Admin admin = findAdminByUserId(userId);
        return admin.getRegions();
    }

    public Admin findAdminByUserId(Long userId) {
        return adminDao.findAdminByUserId(userId).orElseThrow(AdminNullException::new);
    }

    public List<Admin> findAdminsByRegion(Region region){

        List<Admin> allAdmins = adminDao.findAll();
        return allAdmins.stream().filter(e -> e.getRegions().contains(region)).collect(Collectors.toList());
    }

    public boolean isExistedUser(User user) {
        return adminDao.existsAdminByUser(user);
    }

    public boolean isExistedByUserId(Long userId){
        return adminDao.existsAdminByUser_UserId(userId);
    }

    public void deleteAll() {
        adminDao.deleteAll();
    }
}
