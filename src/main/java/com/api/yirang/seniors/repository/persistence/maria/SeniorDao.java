package com.api.yirang.seniors.repository.persistence.maria;

import com.api.yirang.common.support.type.Region;
import com.api.yirang.common.support.type.Sex;
import com.api.yirang.seniors.domain.senior.model.Senior;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    // update
    @Modifying
    @Transactional
    @Query(
            "UPDATE Senior S " +
            "SET S.seniorName =:name, S.sex =:sex, " +
            "    S.address =:address, S.phone =:phone, " +
            "    S.region =:region " +
            "WHERE S =:existedSenior"
    )
    void updateSenior(Senior existedSenior, String name, Sex sex, String address, String phone, Region region);

    // delete
}
