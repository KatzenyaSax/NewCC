package com.dafuweng.sales.domain.vo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class WorkLogVO {
    private Long id;
    private Long salesRepId;
    private String salesRepName;
    private LocalDate logDate;
    private Integer callsMade;
    private Integer effectiveCalls;
    private Integer intentionClients;
    private Integer faceToFaceClients;
    private String content;
    private LocalDateTime createdAt;
}
