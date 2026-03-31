package com.dafuweng.sales.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("contact_record")
public class ContactRecord implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 客户ID */
    private Long customerId;

    /** 销售ID */
    private Long salesRepId;

    /** 联系类型：1-电话 2-面谈 3-转介绍 */
    private Integer contactType;

    /** 洽谈内容 */
    private String content;

    /** 洽谈后意向等级 */
    private Integer intentionAfter;

    /** 联系时间 */
    private LocalDateTime contactDate;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /** 逻辑删除：0-未删 1-已删 */
    @TableLogic
    private Integer deleted;
}
