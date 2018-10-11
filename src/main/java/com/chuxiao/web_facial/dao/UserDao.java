package com.chuxiao.web_facial.dao;

import com.chuxiao.web_facial.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserDao extends JpaRepository<User, Long> {

    @Query("select username from User")
    List<String> getAllUsername();

    List<User> findAll();

//    User getUserByUsername(String username);

    User findByUsername(String username);

    @Query("select password from User where username=?1")
    String getPasswordByUsername(String username);

    @Query("select username,nickname,password,sex from User where username=?1")
    User getUserByUsername(String username);

    @Query("select username from User where username=?1")
    String getUsernameByUsername(String username);

    @Query("select nickname from User where username=?1")
    String getNicknameByUsername(String username);
}
