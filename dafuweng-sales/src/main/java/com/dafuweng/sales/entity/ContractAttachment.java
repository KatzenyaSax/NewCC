package com.dafuweng.sales.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("contract_attachment")
public class ContractAttachment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 合同ID */
    private Long contractId;

    /** 附件类型：business_license/id_card/other */
    private String attachmentType;

    /** 文件URL */
    private String fileUrl;

    /** 原始文件名 */
    private String fileName;

    /** 文件大小(字节) */
    private Long fileSize;

    /** 上传时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime uploadTime;

    /** 逻辑删除：0-未删 1-已删 */
    @TableLogic
    private Integer deleted;
}
