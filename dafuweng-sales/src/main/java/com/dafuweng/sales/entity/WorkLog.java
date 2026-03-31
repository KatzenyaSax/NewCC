package com.dafuweng.sales.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("work_log")
public class WorkLog implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 销售ID */
    private Long salesRepId;

    /** 日志日期 */
    private LocalDate logDate;

    /** 打电话数 */
    private Integer callsMade;

    /** 有效电话数 */
    private Integer effectiveCalls;

    /** 意向客户数 */
    private Integer intentionClients;

    /** 面谈客户数 */
    private Integer faceToFaceClients;

    /** 备注内容 */
    private String content;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /** 逻辑删除：0-未删 1-已删 */
    @TableLogic
    private Integer deleted;
}
