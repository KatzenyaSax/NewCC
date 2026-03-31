package com.dafuweng.sales.domain.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ContactRecordCreateRequest {

    @NotNull(message = "客户ID不能为空")
    private Long customerId;

    /** 联系类型：1-电话 2-面谈 3-转介绍 */
    @NotNull(message = "联系类型不能为空")
    @Min(value = 1, message = "联系类型不合法")
    @Max(value = 3, message = "联系类型不合法")
    private Integer contactType;

    @NotNull(message = "联系时间不能为空")
    private LocalDateTime contactDate;

    @NotBlank(message = "洽谈内容不能为空")
    @Size(max = 2000, message = "洽谈内容最多2000字符")
    private String content;

    /** 洽谈前意向等级 */
    @Min(value = 1, message = "意向等级不合法")
    @Max(value = 4, message = "意向等级不合法")
    private Integer intentionBefore;

    /** 洽谈后意向等级 */
    @Min(value = 1, message = "意向等级不合法")
    @Max(value = 4, message = "意向等级不合法")
    private Integer intentionAfter;

    /** 下次跟进日期 */
    private LocalDate followUpDate;

    /** 附件URLs（JSON数组） */
    private List<String> attachmentUrls;
}
