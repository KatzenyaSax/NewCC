package com.dafuweng.sales.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("customer")
public class Customer implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 客户姓名 */
    private String name;

    /** 手机号 */
    private String phone;

    /** 身份证号 */
    private String idCard;

    /** 企业名称（企业客户） */
    private String companyName;

    /** 客户类型：1-个人 2-企业 */
    private Integer customerType;

    /** 负责销售ID */
    private Long salesRepId;

    /** 所属部门ID */
    private Long deptId;

    /** 所属战区ID */
    private Long zoneId;

    /** 意向等级：1-A 2-B 3-C 4-D */
    private Integer intentionLevel;

    /** 状态：1-潜在 2-洽谈中 3-已签约 4-已放款 5-公海 */
    private Integer status;

    /** 最后联系日期 */
    private LocalDateTime lastContactDate;

    /** 进入公海时间 */
    private LocalDateTime publicSeaTime;

    /** 批注（JSON数组） */
    private String annotation;

    /** 创建人 */
    private Long createdBy;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /** 修改人 */
    private Long updatedBy;

    /** 修改时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /** 逻辑删除：0-未删 1-已删 */
    @TableLogic
    private Integer deleted;

    /** 乐观锁版本 */
    @Version
    private Integer version;
}
