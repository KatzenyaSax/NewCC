package com.dafuweng.sales.enums;

import lombok.Getter;

@Getter
public enum CustomerType {
    PERSONAL(1, "个人"),
    ENTERPRISE(2, "企业");

    private final int code;
    private final String desc;

    CustomerType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static CustomerType fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (CustomerType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
