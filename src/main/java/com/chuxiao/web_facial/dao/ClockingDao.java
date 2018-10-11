package com.chuxiao.web_facial.dao;

import com.chuxiao.web_facial.entity.Clocking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface ClockingDao extends JpaRepository<Clocking, Long> {

//    @Query("select c.createDate from Clocking c,User u where u.username=?1")
//    List<Date> queryClockingsByUsername(String username);

//    Page<T> findAll(Pageable var1);

    @Query("select c.createDate from Clocking c,User u where u.username=?1 and u.id=c.user")
    Page<Date> queryClockingsByUsername(String username, Pageable pageable);

}
