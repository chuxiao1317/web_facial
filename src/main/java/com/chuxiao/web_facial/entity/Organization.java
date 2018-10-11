package com.chuxiao.web_facial.entity;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "facial_organization")
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date createDate;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date updateDate;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "organization_id")//单向在"一方"标记此注解
    private Set<User> users = new HashSet<>();

    public Organization() {
    }

    public Organization(String name, Date createDate, Date updateDate) {
        this.name = name;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
