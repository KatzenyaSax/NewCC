package com.dafuweng.common.mybatis.config;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import org.apache.ibatis.executor.statement.StatementHandler;

/**
 * 数据权限拦截器
 *
 * 【强制】所有查询自动根据用户角色拼接数据权限 SQL
 * 数据范围规则：
 * - 销售代表（sales_rep）：只能看自己的客户
 * - 部门经理（dept_manager）：看本部门所有客户
 * - 销售总监（sales_director）：看本战区所有客户
 * - 总经理/管理员：看全部（不拼接限制）
 *
 * 使用方式：在 Mapper XML 或 @Select 注解中标记表别名
 * 例如：SELECT * FROM customer c → 自动拼接 c.sales_rep_id = #{userId}
 */
@Slf4j
public class DataScopeInterceptor implements InnerInterceptor {

    /**
     * 当前用户上下文（由 JWT Filter 写入 ThreadLocal）
     */
    private static final ThreadLocal<DataScope> DATA_SCOPE_HOLDER = new ThreadLocal<>();

    /**
     * 设置当前用户的数据权限范围
     */
    public static void setDataScope(DataScope dataScope) {
        DATA_SCOPE_HOLDER.set(dataScope);
    }

    /**
     * 清除当前数据权限上下文
     */
    public static void clearDataScope() {
        DATA_SCOPE_HOLDER.remove();
    }

    @Override
    public void beforeQuery(StatementHandler statementHandler) {
        // 获取当前数据权限上下文
        DataScope dataScope = DATA_SCOPE_HOLDER.get();
        if (dataScope == null) {
            return; // 未登录用户，不做限制
        }

        // TODO: 根据 dataScope 构建数据权限 SQL
        // PluginUtils.mp().sqlFirstComment()
        // String originalSql = statementHandler.getBoundSql().getSql();
        // 动态拼接 WHERE 条件
    }

    /**
     * 数据权限上下文
     */
    public record DataScope(
        Long userId,
        String role,
        Long deptId,
        Long zoneId
    ) {}
}
