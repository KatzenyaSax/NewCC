package com.dafuweng.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("sys_department")
public class SysDepartment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String deptCode;

    private String deptName;

    private Long parentId;

    private Long zoneId;

    private Long managerId;

    private Integer sortOrder;

    private Integer status;

    private Long createdBy;

    private LocalDateTime createdAt;

    private Long updatedBy;

    private LocalDateTime updatedAt;

    @TableLogic
    private Integer deleted;
}
