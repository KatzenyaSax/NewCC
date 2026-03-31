package com.dafuweng.sales.enums;

import lombok.Getter;

@Getter
public enum IntentionLevel {
    A(1, "A级(高)"),
    B(2, "B级(中)"),
    C(3, "C级(低)"),
    D(4, "D级(无)");

    private final int code;
    private final String desc;

    IntentionLevel(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static IntentionLevel fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (IntentionLevel level : values()) {
            if (level.code.equals(code)) {
                return level;
            }
        }
        return null;
    }
}
