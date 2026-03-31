package com.dafuweng.system.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OperationLogVO {
    private Long id;
    private Long userId;
    private String username;
    private String module;
    private String action;
    private String requestMethod;
    private String requestUrl;
    private String requestParams;
    private String responseCode;
    private String errorMsg;
    private String ip;
    private String userAgent;
    private Long costTimeMs;
    private LocalDateTime createdAt;
}
