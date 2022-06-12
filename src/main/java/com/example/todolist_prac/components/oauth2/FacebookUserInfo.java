package com.example.todolist_prac.components.oauth2;

import lombok.Data;

import java.util.Map;

@Data
public class FacebookUserInfo implements OAuth2UserInfo {
    private Map<String, Object> attributes; // oauth2User.getAttributes()

    public FacebookUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getProvider() {
        return "facbook";
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
}
