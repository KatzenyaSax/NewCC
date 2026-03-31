package com.dafuweng.sales.domain.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CustomerCreateRequest {

    @NotBlank(message = "客户姓名不能为空")
    @Size(max = 100, message = "客户姓名最多100字符")
    private String name;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Size(max = 20, message = "身份证号最多20字符")
    private String idCard;

    /** 客户类型：1-个人 2-企业 */
    @NotNull(message = "客户类型不能为空")
    @Min(value = 1, message = "客户类型不合法")
    @Max(value = 2, message = "客户类型不合法")
    private Integer customerType;

    /** 企业客户必填 */
    @Size(max = 200, message = "企业名称最多200字符")
    private String companyName;

    @Size(max = 100, message = "企业法人最多100字符")
    private String companyLegalPerson;

    /** 意向等级：1-A 2-B 3-C 4-D */
    @Min(value = 1, message = "意向等级不合法")
    @Max(value = 4, message = "意向等级不合法")
    private Integer intentionLevel;

    @DecimalMin(value = "0", message = "意向金额不能为负")
    private BigDecimal loanIntentionAmount;

    @Size(max = 100, message = "意向产品最多100字符")
    private String loanIntentionProduct;

    /** 客户来源 */
    @Size(max = 50, message = "客户来源最多50字符")
    private String source;
}
