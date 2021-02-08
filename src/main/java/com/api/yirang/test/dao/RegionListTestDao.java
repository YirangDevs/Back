package com.api.yirang.test.dao;

import com.api.yirang.test.model.RegionListTestModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RegionListTestDao extends JpaRepository<RegionListTestModel, Long> {

    @Override
    Optional<RegionListTestModel> findById(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE RegionListTestModel R " +
           "SET R.regions =:regions " +
           "WHERE R.id =:id")
    void updateRegionsByUserId(Long id, String regions);

    @Override
    <S extends RegionListTestModel> S save(S s);
}
