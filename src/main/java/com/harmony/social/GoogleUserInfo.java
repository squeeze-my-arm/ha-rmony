package com.harmony.social;

import lombok.Getter;

@Getter
public class GoogleUserInfo {
    private String id;
    private String email;
    private String name;

    public void setName(String randomAnimalName) {
        this.name = randomAnimalName;
    }
}
