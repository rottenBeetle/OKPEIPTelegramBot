package com.rottenbeetle.newsletterokpeip.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Role {
    ADMIN("Учитель"),USER("Ученик");

    private String role;

    @Override
    public String toString() {
        return role;
    }
}
