package com.dafuweng.sales.enums;

import lombok.Getter;

@Getter
public enum ContactType {
    PHONE(1, "电话"),
    FACE_TO_FACE(2, "面谈"),
    REFERRAL(3, "转介绍");

    private final int code;
    private final String desc;

    ContactType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ContactType fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ContactType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
