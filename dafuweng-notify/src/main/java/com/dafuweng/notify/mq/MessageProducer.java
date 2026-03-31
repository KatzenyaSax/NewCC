package com.dafuweng.notify.mq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 消息生产者
 *
 * 【强制】所有异步事件必须通过本类发送
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MessageProducer {

    private final RabbitTemplate rabbitTemplate;

    // ================================================================
    // 合同事件
    // ================================================================

    /**
     * 发送合同签署事件
     */
    public void sendContractSigned(Long contractId, Long customerId, Long salesRepId) {
        Map<String, Object> message = Map.of(
            "eventType", "CONTRACT_SIGNED",
            "contractId", contractId,
            "customerId", customerId,
            "salesRepId", salesRepId,
            "timestamp", java.time.Instant.now().toString()
        );
        rabbitTemplate.convertAndSend("contract.exchange", "contract.signed", message);
        log.info("发送合同签署事件: contractId={}", contractId);
    }

    // ================================================================
    // 贷款事件
    // ================================================================

    /**
     * 发送贷款批准事件
     */
    public void sendLoanApproved(Long contractId, Long customerId, Long salesRepId) {
        Map<String, Object> message = Map.of(
            "eventType", "LOAN_APPROVED",
            "contractId", contractId,
            "customerId", customerId,
            "salesRepId", salesRepId,
            "timestamp", java.time.Instant.now().toString()
        );
        rabbitTemplate.convertAndSend("loan.exchange", "loan.approved", message);
        log.info("发送贷款批准事件: contractId={}", contractId);
    }

    /**
     * 发送贷款拒绝事件
     */
    public void sendLoanRejected(Long contractId, Long customerId, Long salesRepId, String reason) {
        Map<String, Object> message = Map.of(
            "eventType", "LOAN_REJECTED",
            "contractId", contractId,
            "customerId", customerId,
            "salesRepId", salesRepId,
            "reason", reason,
            "timestamp", java.time.Instant.now().toString()
        );
        rabbitTemplate.convertAndSend("loan.exchange", "loan.rejected", message);
        log.info("发送贷款拒绝事件: contractId={}, reason={}", contractId, reason);
    }

    // ================================================================
    // 公海事件
    // ================================================================

    /**
     * 发送客户入公海延迟消息
     *
     * @param customerId 客户ID
     * @param delayDays  延迟天数（TTL）
     */
    public void sendCustomerPublicSeaDelay(Long customerId, int delayDays) {
        long ttlMillis = delayDays * 24L * 60L * 60L * 1000L;
        rabbitTemplate.convertAndSend("delay.exchange", "customer.publicsea.delay.routing.key",
            Map.of("eventType", "CUSTOMER_PUBLIC_SEA_CHECK", "customerId", customerId),
            message -> {
                message.getMessageProperties().setExpiration(String.valueOf(ttlMillis));
                return message;
            });
        log.info("发送客户入公海延迟消息: customerId={}, delayDays={}", customerId, delayDays);
    }

    /**
     * 发送客户入公海事件（立即通知）
     */
    public void sendCustomerPublicSea(Long customerId, Long originalSalesRepId) {
        Map<String, Object> message = Map.of(
            "eventType", "CUSTOMER_PUBLIC_SEA",
            "customerId", customerId,
            "originalSalesRepId", originalSalesRepId,
            "timestamp", java.time.Instant.now().toString()
        );
        rabbitTemplate.convertAndSend("publicsea.exchange", "customer.publicsea.routing.key", message);
        log.info("发送客户入公海事件: customerId={}", customerId);
    }
}
