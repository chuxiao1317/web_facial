package com.chuxiao.web_facial.dao;

import com.chuxiao.web_facial.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface OrganizationDao extends JpaRepository<Organization, Long>, JpaSpecificationExecutor<Organization> {
    Organization getOrganizationByName(String name);

    @Query("select name, createDate, updateDate from Organization ")
    List<Organization> getOrganizations();

    @Transactional
    void deleteOrganizationByName(String organizationName);

    @Modifying
    @Transactional
    @Query("update Organization set name =?1,updateDate=?2 where name=?3")
    void updateOrganizationByName(String newName, Date updateDate, String previousName);

    @Query("select name from Organization where name=?1")
    String findNameByName(String organizationName);

    @Query("select name from Organization")
    Set<String> getNames();
}
