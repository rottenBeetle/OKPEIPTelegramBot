package com.rottenbeetle.newsletterokpeip.botapi.handlers.fillingprofile;

import lombok.Data;


/**
 * Здесь хранятся данные о группе пользователя
 */
@Data
public class  UserProfileData {
    private String group;
    private String name;
    private String age;
}
