package com.DormitoryBack.domain.member.restriction.domain.enums;

public enum Functions {
    USER(1),
    ARTICLE(2),
    COMMENT(4),
    GROUP(8);

    private final int value;

    Functions(int value){
        this.value=value;
    }

    public int getValue(){
        return value;
    }
}
