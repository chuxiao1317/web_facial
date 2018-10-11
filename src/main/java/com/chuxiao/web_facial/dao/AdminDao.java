package com.chuxiao.web_facial.dao;

import com.chuxiao.web_facial.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdminDao extends JpaRepository<Admin, Long> {

    @Query("select password from Admin where username=?1")
    String getPasswordByUsername(String username);
}
