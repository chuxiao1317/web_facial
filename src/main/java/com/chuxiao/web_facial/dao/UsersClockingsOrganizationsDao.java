package com.chuxiao.web_facial.dao;

import com.chuxiao.web_facial.entity.UsersClockingsOrganizations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UsersClockingsOrganizationsDao extends JpaRepository<UsersClockingsOrganizations, Long> {

//    @Query("select distinct u.nickname, o.name, c.createDate from Organization o,User u, Clocking c where u.organization.id = o.id and u.id = c.user.id order by c.createDate desc")
//    Page<UsersClockingsOrganizations> queryClockings(Pageable pageable);

    @Query("select distinct u.nickname, o.name, c.createDate from Organization o,User u, Clocking c where u.organization.id = o.id and u.id = c.user.id order by c.createDate desc")
    List<UsersClockingsOrganizations> queryClockings();

}
