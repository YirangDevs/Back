package com.api.yirang.seniors.repository.persistence.maria;

import com.api.yirang.common.domain.region.model.Region;
import com.api.yirang.seniors.domain.senior.model.Senior;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface SeniorDao extends JpaRepository<Senior, Long> {


    // Find

    Optional<Senior> findSeniorByPhone(String phone);

    Collection<Senior> findSeniorsByRegion(Region region);
    // isExist
    boolean existsSeniorByPhone(String phone);

    // Count
    Long countSeniorsByRegion(Region region);



    // update

    // delete
}
