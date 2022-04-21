package com.rottenbeetle.newsletterokpeip.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Подписка пользователя на определенную группу
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "user_subscriptions")
public class UserSubscription {
    @Id
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "chat_id")
    private Long chatId;
    @Column(name = "group_name")
    private String groupName;

    public UserSubscription(Long userId, Long chatId, String groupName) {
        this.userId = userId;
        this.chatId = chatId;
        this.groupName = groupName;
    }
}
