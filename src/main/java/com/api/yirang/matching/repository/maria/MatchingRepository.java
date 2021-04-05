package com.api.yirang.matching.repository.maria;

import com.api.yirang.matching.model.maria.Matching;
import com.api.yirang.notices.domain.activity.model.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchingRepository extends JpaRepository<Matching, Long> {

    List<Matching> findMatchingsByActivity(Activity activity);
}
