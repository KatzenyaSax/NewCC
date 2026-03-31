package com.dafuweng.sales.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ContractSignRequest {

    @NotNull(message = "合同ID不能为空")
    private Long contractId;

    /** 纸质合同编号 */
    @NotBlank(message = "纸质合同编号不能为空")
    @Size(max = 100, message = "纸质合同编号最多100字符")
    private String paperContractNo;

    /** 签署日期 */
    @NotNull(message = "签署日期不能为空")
    private LocalDate signDate;

    /** 附件列表 */
    @NotEmpty(message = "至少需要上传一个附件")
    private List<ContractAttachmentRequest> attachments;
}
