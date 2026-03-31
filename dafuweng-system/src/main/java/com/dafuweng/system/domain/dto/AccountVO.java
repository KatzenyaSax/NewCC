package com.dafuweng.system.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AccountVO {
    private Long id;
    private String username;
    private String realName;
    private String phone;
    private String email;
    private Long deptId;
    private String deptName;
    private Long zoneId;
    private String zoneName;
    private Integer status;
    private String statusDesc;
    private String lastLoginTime;
    private String lastLoginIp;
    private List<RoleSimpleVO> roles;
    private LocalDateTime createdAt;
}
