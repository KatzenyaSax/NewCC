package com.dafuweng.finance.enums;

import lombok.Getter;

@Getter
public enum BankAuditStatus {
    PENDING("pending", "待审核"),
    IN_REVIEW("in_review", "审核中"),
    APPROVED("approved", "已通过"),
    REJECTED("rejected", "已拒绝");

    private final String code;
    private final String desc;

    BankAuditStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static BankAuditStatus fromCode(String code) {
        if (code == null) return null;
        for (BankAuditStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}
