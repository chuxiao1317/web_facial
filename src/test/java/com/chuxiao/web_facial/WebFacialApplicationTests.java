package com.chuxiao.web_facial;

import com.chuxiao.web_facial.dao.UserDao;
import com.chuxiao.web_facial.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WebFacialApplicationTests {

    @Autowired
    private UserDao userDao;

    @Test
    public void contextLoads() {
//        userDao.save(new User("褚枭", "褚枭", "褚枭", "褚枭", "褚枭"));
//        List<User> users = userDao.findAll();
//        for (User user : users) {
//            System.out.println("从数据库查到的名字：" + user.getNickname());
//        }
    }

}
