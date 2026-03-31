package com.dafuweng.sales.enums;

import lombok.Getter;

@Getter
public enum ContractStatus {
    DRAFT(1, "草稿"),
    SIGNED(2, "已签署"),
    FIRST_FEE_PAID(3, "已支付首期"),
    IN_AUDIT(4, "审核中"),
    APPROVED(5, "已通过"),
    REJECTED(6, "已拒绝"),
    LOAN_GRANTED(7, "已放款"),
    COMPLETED(8, "已完成");

    private final int code;
    private final String desc;

    ContractStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ContractStatus fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ContractStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }

    /**
     * 状态流转校验
     */
    public boolean canTransitionTo(ContractStatus target) {
        return switch (this) {
            case DRAFT -> target == SIGNED;
            case SIGNED -> target == FIRST_FEE_PAID;
            case FIRST_FEE_PAID -> target == IN_AUDIT;
            case IN_AUDIT -> target == APPROVED || target == REJECTED;
            case APPROVED -> target == LOAN_GRANTED;
            case LOAN_GRANTED -> target == COMPLETED;
            default -> false;
        };
    }
}
