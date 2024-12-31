package com.DormitoryBack.domain.member.domain.entity;

public enum ProviderType {
    LOCAL, GOOGLE;

    public static ProviderType getProviderType(String provider){
        if(provider==null){
            throw new RuntimeException("ProviderCannotBeNull");
        }

    return switch (provider.toLowerCase()){
        case "google" -> ProviderType.GOOGLE;
        default -> throw new RuntimeException("ProvierNotFound");
    };
    }
}
