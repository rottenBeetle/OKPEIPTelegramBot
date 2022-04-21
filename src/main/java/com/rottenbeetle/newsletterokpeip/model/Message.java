package com.rottenbeetle.newsletterokpeip.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "group_name")
    private String group;
    @Column(name = "message")
    private String message;

    public Message(String group, String message) {
        this.group = group;
        this.message = message;
    }
}
