package com.dafuweng.finance.enums;

import lombok.Getter;

@Getter
public enum AuditStatus {
    PENDING(1, "待审核"),
    IN_PROGRESS(2, "审核中"),
    APPROVED(3, "已通过"),
    REJECTED(4, "已拒绝");

    private final int code;
    private final String desc;

    AuditStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static AuditStatus fromCode(Integer code) {
        if (code == null) return null;
        for (AuditStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}
