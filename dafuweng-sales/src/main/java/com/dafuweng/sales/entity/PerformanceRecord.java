package com.dafuweng.sales.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("performance_record")
public class PerformanceRecord implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 销售ID */
    private Long salesRepId;

    /** 部门ID */
    private Long deptId;

    /** 战区ID */
    private Long zoneId;

    /** 合同ID */
    private Long contractId;

    /** 合同金额 */
    private BigDecimal contractAmount;

    /** 提成比例 */
    private BigDecimal commissionRate;

    /** 提成金额 */
    private BigDecimal commissionAmount;

    /** 状态：1-计算中 2-已确认 3-已发放 */
    private Integer status;

    /** 计算时间 */
    private LocalDateTime calculatedAt;

    /** 确认时间 */
    private LocalDateTime confirmedAt;

    /** 发放时间 */
    private LocalDateTime grantedAt;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /** 逻辑删除：0-未删 1-已删 */
    @TableLogic
    private Integer deleted;
}
