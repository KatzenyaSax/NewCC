package com.dafuweng.finance.domain.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ServiceFeeRecordCreateRequest {

    @NotNull(message = "合同ID不能为空")
    private Long contractId;

    @NotNull(message = "费用类型不能为空")
    @Min(value = 1, message = "费用类型不合法")
    @Max(value = 2, message = "费用类型不合法")
    private Integer feeType;

    @NotNull(message = "实收金额不能为空")
    @DecimalMin(value = "0", message = "实收金额不能为负")
    private BigDecimal amount;

    @NotNull(message = "应收金额不能为空")
    @DecimalMin(value = "0", message = "应收金额不能为负")
    private BigDecimal shouldAmount;

    @Pattern(regexp = "^(bank_transfer|wechat|alipay|cash)$", message = "支付方式不合法")
    private String paymentMethod;

    private LocalDate paymentDate;

    @Size(max = 100, message = "付款账户最多100字符")
    private String paymentAccount;

    @Size(max = 50, message = "收据编号最多50字符")
    private String receiptNo;

    @Size(max = 500, message = "备注最多500字符")
    private String remark;
}
