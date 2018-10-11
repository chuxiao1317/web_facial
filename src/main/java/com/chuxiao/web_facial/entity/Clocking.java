package com.chuxiao.web_facial.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "facial_clocking")
public class Clocking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Date createDate;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
    @JoinColumn(name = "user_id")// 双向
    private User user;

    public Clocking(Date createDate, User user) {
        this.createDate = createDate;
        this.user = user;
    }

    public Clocking() {
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}