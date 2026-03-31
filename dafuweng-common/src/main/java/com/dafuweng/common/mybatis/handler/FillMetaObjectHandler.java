package com.dafuweng.common.mybatis.handler;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatisPlus 自动填充处理器
 *
 * 【强制】所有实体表的 created_at/updated_at 必须由本类自动填充
 * 【强制】created_by/updated_by 必须由 SecurityContextHolder 获取当前用户ID填充
 */
@Component
public class FillMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        // 创建时间
        this.strictInsertFill(metaObject, "createdAt", LocalDateTime.class, LocalDateTime.now());
        // 修改时间
        this.strictInsertFill(metaObject, "updatedAt", LocalDateTime.class, LocalDateTime.now());
        // 逻辑删除（默认0）
        this.strictInsertFill(metaObject, "deleted", Integer.class, 0);

        // 从 SecurityContext 获取当前用户ID（如果可用）
        Long currentUserId = getCurrentUserId();
        if (currentUserId != null) {
            this.strictInsertFill(metaObject, "createdBy", Long.class, currentUserId);
            this.strictInsertFill(metaObject, "updatedBy", Long.class, currentUserId);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 修改时间
        this.strictUpdateFill(metaObject, "updatedAt", LocalDateTime.class, LocalDateTime.now());

        // 从 SecurityContext 获取当前用户ID（如果可用）
        Long currentUserId = getCurrentUserId();
        if (currentUserId != null) {
            this.strictUpdateFill(metaObject, "updatedBy", Long.class, currentUserId);
        }
    }

    /**
     * 从当前线程上下文获取用户ID
     * 具体实现依赖于安全框架（JWT Filter 或 Spring Security）
     */
    private Long getCurrentUserId() {
        try {
            // 尝试从 ThreadLocal 获取（JWT Filter 写入）
            Object userId = cn.hutool.core.thread.ThreadLocalUtil.get("userId");
            if (userId != null) {
                return Long.valueOf(userId.toString());
            }
        } catch (Exception ignored) {
        }
        return null;
    }
}
