package com.api.yirang.apply.repository.persistence.maria;

import com.api.yirang.apply.domain.model.Apply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by JeongminYoo on 2020/12/30
 * Work with Yirang
 * Email: likemin0142@gmail.com
 * Blog: http://Beewoom.github.io
 * Github: http://github.com/Biewoom
 */
@Repository
public interface ApplyDao extends JpaRepository<Apply, Long> {

}
