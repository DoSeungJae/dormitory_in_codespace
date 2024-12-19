package com.DormitoryBack.domain.member.restriction.domain.enums;

public enum Function {
    LOGIN(1),
    ARTICLE(2),
    COMMENT(4),
    GROUP(8);

    private final int value;

    Function(int value){
        this.value=value;
    }

    public int getValue(){
        return value;
    }

    public static Function fromString(String function) {
        return Function.valueOf(function.toUpperCase());
    }
}

