package com.api.yirang.auth.repository.persistence.maria;

import com.api.yirang.auth.domain.user.model.User;
import com.api.yirang.auth.support.type.Authority;
import com.api.yirang.common.support.type.Region;
import com.api.yirang.common.support.type.Sex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserDao extends JpaRepository<User, Long> {
    Optional<User> findByUserId(Long userId);
    boolean existsUserByUserId(Long userId);

    @Modifying
    void deleteByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE User u set u.authority =:authority " +
           "WHERE u.userId =:userId")
    void updateAuthority(Long userId, Authority authority);

    @Modifying
    @Transactional
    @Query("UPDATE User u " +
           "set u.email =:email, u.phone =:phone, u.username =:username," +
           "    u.realname =:realname, u.sex =:sex, u.firstRegion =:firstRegion, u.secondRegion =:secondRegion " +
           "WHERE u.userId =:userId")
    void updateUserInfo(Long userId, String email, String phone, String username,
                        String realname, Sex sex, Region firstRegion, Region secondRegion);


    List<User> findAllByAuthority(Authority authority);
}
