package com.dafuweng.auth.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * 用户信息VO
 */
@Data
public class UserVO {

    private Long userId;

    private String username;

    private String realName;

    private String phone;

    private String email;

    private Long deptId;

    private String deptName;

    private Long zoneId;

    private String zoneName;

    private Integer status;

    private List<String> roles;

    private List<String> permissions;
}
