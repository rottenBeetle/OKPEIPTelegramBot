package com.rottenbeetle.newsletterokpeip.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @Column(name = "user_id")
    private long user_id;
    @Column(name = "chat_id")
    private long chat_id;
    @Column(name = "groupname")
    private String group;
    @Column(name = "username")
    private String userName;

    public User() {
    }

    public User(long user_id, long chat_id, String group, String userName) {
        this.user_id = user_id;
        this.chat_id = chat_id;
        this.group = group;
        this.userName = userName;
    }
}
