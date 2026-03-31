package com.dafuweng.sales.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("contract")
public class Contract implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 合同编号 */
    private String contractNo;

    /** 客户ID */
    private Long customerId;

    /** 销售ID */
    private Long salesRepId;

    /** 部门ID */
    private Long deptId;

    /** 金融产品ID（审核后填充） */
    private Long productId;

    /** 合同金额 */
    private BigDecimal contractAmount;

    /** 实际放款金额（银行放款后） */
    private BigDecimal actualLoanAmount;

    /** 服务费率 */
    private BigDecimal serviceFeeRate;

    /** 首期服务费 */
    private BigDecimal serviceFee1;

    /** 二期服务费 */
    private BigDecimal serviceFee2;

    /** 首期是否已付：0-否 1-是 */
    private Integer serviceFee1Paid;

    /** 二期是否已付：0-否 1-是 */
    private Integer serviceFee2Paid;

    /** 状态：1-草稿 2-已签署 3-已支付首期 4-审核中 5-已通过 6-已拒绝 7-已放款 8-已完成 */
    private Integer status;

    /** 签署日期 */
    private LocalDate signDate;

    /** 发送至金融部时间 */
    private LocalDateTime financeSendTime;

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
}
