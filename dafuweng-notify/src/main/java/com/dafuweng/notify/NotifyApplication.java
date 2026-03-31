package com.dafuweng.notify;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 消息通知服务启动类
 *
 * 端口: 8085
 * 职责: 异步消息处理、定时任务（RabbitMQ）
 */
@SpringBootApplication
@MapperScan("com.dafuweng.notify.mapper")
public class NotifyApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotifyApplication.class, args);
    }
}
