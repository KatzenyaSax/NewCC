package com.dafuweng.sales.enums;

import lombok.Getter;

@Getter
public enum CustomerStatus {
    POTENTIAL(1, "潜在"),
    NEGOTIATING(2, "洽谈中"),
    SIGNED(3, "已签约"),
    LOANED(4, "已放款"),
    PUBLIC_SEA(5, "公海");

    private final int code;
    private final String desc;

    CustomerStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static CustomerStatus fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (CustomerStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}
