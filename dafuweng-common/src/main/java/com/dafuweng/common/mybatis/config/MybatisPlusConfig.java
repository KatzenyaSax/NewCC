package com.dafuweng.common.mybatis.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.dafuweng.common.mybatis.handler.FillMetaObjectHandler;
import com.dafuweng.common.mybatis.handler.JsonTypeHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * MyBatisPlus 全局配置
 *
 * 配置内容：
 * 1. 分页插件（PaginationInnerInterceptor）
 * 2. 逻辑删除插件（已在 application.yml 配置）
 * 3. 自动填充处理器（创建时间/创建人/修改时间/修改人）
 * 4. JSON字段处理器（用于 annotation 等 JSON 字段）
 */
@Configuration
@MapperScan("com.dafuweng.**.mapper")
public class MybatisPlusConfig {

    /**
     * 分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * 自动填充处理器
     * 用于自动填充 created_at, updated_at, created_by, updated_by
     */
    @Bean
    @ConditionalOnMissingBean(MetaObjectHandler.class)
    public MetaObjectHandler metaObjectHandler() {
        return new FillMetaObjectHandler();
    }
}
