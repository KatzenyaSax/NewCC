package com.dafuweng.sales.domain.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CustomerUpdateRequest {

    @NotNull(message = "客户ID不能为空")
    private Long id;

    @Size(max = 100, message = "客户姓名最多100字符")
    private String name;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Size(max = 20, message = "身份证号最多20字符")
    private String idCard;

    @Min(value = 1, message = "客户类型不合法")
    @Max(value = 2, message = "客户类型不合法")
    private Integer customerType;

    @Size(max = 200, message = "企业名称最多200字符")
    private String companyName;

    @Min(value = 1, message = "意向等级不合法")
    @Max(value = 4, message = "意向等级不合法")
    private Integer intentionLevel;

    /** 下次跟进日期 */
    private LocalDate nextFollowUpDate;

    @DecimalMin(value = "0", message = "意向金额不能为负")
    private BigDecimal loanIntentionAmount;

    @Size(max = 100, message = "意向产品最多100字符")
    private String loanIntentionProduct;
}
