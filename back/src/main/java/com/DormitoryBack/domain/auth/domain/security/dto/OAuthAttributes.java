package com.DormitoryBack.domain.auth.domain.security.dto;

import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttributes {
    private final Map<String, Object> attributes;
    private final String registrationId;
    private final String nameAttributeKey;
    private final String nickname;
    private final String email;
    //private final String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String email, String nickname, String nameAttributeKey,  String registrationId) {
        this.attributes = attributes;
        this.email = email;
        this.nickname = nickname;
        this.nameAttributeKey = nameAttributeKey;
        //this.picture = picture;
        this.registrationId = registrationId;
    }

    public static OAuthAttributes of(OAuth2UserRequest userRequest, Map<String, Object> attributes) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        if (registrationId == null || userNameAttributeName == null || attributes == null) { 
            throw new IllegalArgumentException("Invalid OAuth2UserRequest or attributes"); }
        return ofGoogle(registrationId, userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nickname((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                //.picture((String) attributes.get("picture"))
                .attributes(attributes)
                .registrationId(registrationId)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
}
