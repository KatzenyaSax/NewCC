package com.dafuweng.finance.enums;

import lombok.Getter;

@Getter
public enum FeeType {
    FIRST_FEE(1, "首期服务费"),
    SECOND_FEE(2, "二期服务费");

    private final int code;
    private final String desc;

    FeeType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static FeeType fromCode(Integer code) {
        if (code == null) return null;
        for (FeeType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
