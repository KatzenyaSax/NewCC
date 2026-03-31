package com.dafuweng.sales.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnnotationVO {
    private Long userId;
    private String userName;
    private String content;
    private LocalDateTime time;
}
