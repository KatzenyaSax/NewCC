package com.dafuweng.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("sys_user")
public class SysAccount implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    private String realName;

    private String phone;

    private String email;

    private Long deptId;

    private Long zoneId;

    private Integer status;

    private Integer loginErrorCount;

    private LocalDateTime lockTime;

    private LocalDateTime lastLoginTime;

    private String lastLoginIp;

    private Long createdBy;

    private LocalDateTime createdAt;

    private Long updatedBy;

    private LocalDateTime updatedAt;

    @TableLogic
    private Integer deleted;

    private Integer version;
}
