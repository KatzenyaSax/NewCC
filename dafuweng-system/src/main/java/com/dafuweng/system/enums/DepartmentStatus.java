package com.dafuweng.system.enums;

import lombok.Getter;

@Getter
public enum DepartmentStatus {
    DISABLED(0, "禁用"),
    ENABLED(1, "启用");

    private final int code;
    private final String desc;

    DepartmentStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static DepartmentStatus of(Integer code) {
        if (code == null) {
            return null;
        }
        for (DepartmentStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        return null;
    }
}
