package com.chuxiao.web_facial.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
public class UsersClockingsOrganizations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date createDate;

    private String nickname;

    private String name;

    public UsersClockingsOrganizations(Date createDate, String nickname, String name) {
        this.createDate = createDate;
        this.nickname = nickname;
        this.name = name;
    }

    public UsersClockingsOrganizations() {
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
