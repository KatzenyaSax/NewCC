package com.dafuweng.finance.enums;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    UNPAID(0, "未付"),
    PAID(1, "已付"),
    PARTIAL(2, "部分付");

    private final int code;
    private final String desc;

    PaymentStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static PaymentStatus fromCode(Integer code) {
        if (code == null) return null;
        for (PaymentStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}
