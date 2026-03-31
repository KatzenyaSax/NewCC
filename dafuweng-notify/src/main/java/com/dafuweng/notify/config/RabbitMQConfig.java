package com.dafuweng.notify.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 配置
 *
 * 交换机和队列定义：
 * - contract.exchange → 合同事件
 * - loan.exchange → 贷款事件
 * - publicsea.exchange → 公海事件
 * - delay.exchange → 延迟交换机（死信队列）
 */
@Configuration
public class RabbitMQConfig {

    // ================================================================
    // 交换机
    // ================================================================
    public static final String CONTRACT_EXCHANGE = "contract.exchange";
    public static final String LOAN_EXCHANGE = "loan.exchange";
    public static final String PUBLIC_SEA_EXCHANGE = "publicsea.exchange";
    public static final String DELAY_EXCHANGE = "delay.exchange";

    // ================================================================
    // 队列
    // ================================================================
    public static final String CONTRACT_SIGNED_QUEUE = "contract.signed.queue";
    public static final String LOAN_APPROVED_QUEUE = "loan.approved.queue";
    public static final String LOAN_REJECTED_QUEUE = "loan.rejected.queue";
    public static final String PUBLIC_SEA_QUEUE = "customer.publicsea.queue";
    public static final String PUBLIC_SEA_DELAY_QUEUE = "customer.publicsea.delay.queue";

    // ================================================================
    // 交换机 Bean
    // ================================================================
    @Bean
    public DirectExchange contractExchange() {
        return new DirectExchange(CONTRACT_EXCHANGE, true, false);
    }

    @Bean
    public DirectExchange loanExchange() {
        return new DirectExchange(LOAN_EXCHANGE, true, false);
    }

    @Bean
    public DirectExchange publicSeaExchange() {
        return new DirectExchange(PUBLIC_SEA_EXCHANGE, true, false);
    }

    @Bean
    public DirectExchange delayExchange() {
        return new DirectExchange(DELAY_EXCHANGE, true, false);
    }

    // ================================================================
    // 队列 Bean
    // ================================================================
    @Bean
    public Queue contractSignedQueue() {
        return new Queue(CONTRACT_SIGNED_QUEUE, true);
    }

    @Bean
    public Queue loanApprovedQueue() {
        return new Queue(LOAN_APPROVED_QUEUE, true);
    }

    @Bean
    public Queue loanRejectedQueue() {
        return new Queue(LOAN_REJECTED_QUEUE, true);
    }

    @Bean
    public Queue publicSeaQueue() {
        return new Queue(PUBLIC_SEA_QUEUE, true);
    }

    /**
     * 公海延迟队列（TTL 后进入死信交换机）
     */
    @Bean
    public Queue publicSeaDelayQueue() {
        return QueueBuilder.durable(PUBLIC_SEA_DELAY_QUEUE)
            .withArgument("x-dead-letter-exchange", PUBLIC_SEA_EXCHANGE)
            .withArgument("x-dead-letter-routing-key", "customer.publicsea.routing.key")
            .build();
    }

    // ================================================================
    // 绑定 Bean
    // ================================================================
    @Bean
    public Binding contractSignedBinding() {
        return BindingBuilder.bind(contractSignedQueue())
            .to(contractExchange())
            .with("contract.signed");
    }

    @Bean
    public Binding loanApprovedBinding() {
        return BindingBuilder.bind(loanApprovedQueue())
            .to(loanExchange())
            .with("loan.approved");
    }

    @Bean
    public Binding loanRejectedBinding() {
        return BindingBuilder.bind(loanRejectedQueue())
            .to(loanExchange())
            .with("loan.rejected");
    }

    @Bean
    public Binding publicSeaBinding() {
        return BindingBuilder.bind(publicSeaQueue())
            .to(publicSeaExchange())
            .with("customer.publicsea.routing.key");
    }

    @Bean
    public Binding publicSeaDelayBinding() {
        return BindingBuilder.bind(publicSeaDelayQueue())
            .to(delayExchange())
            .with("customer.publicsea.delay.routing.key");
    }

    // ================================================================
    // 消息转换器
    // ================================================================
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}
