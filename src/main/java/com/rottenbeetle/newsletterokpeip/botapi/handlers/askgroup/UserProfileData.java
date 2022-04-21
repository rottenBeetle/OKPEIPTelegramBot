package com.rottenbeetle.newsletterokpeip.botapi.handlers.askgroup;

import lombok.Data;
import org.springframework.stereotype.Component;


/**
 * Здесь хранятся данные о группе пользователя
 */

@Data
public class UserProfileData {
    private String group;
    private String message;

}
