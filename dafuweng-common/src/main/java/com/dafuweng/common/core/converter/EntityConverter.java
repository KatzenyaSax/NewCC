package com.dafuweng.common.core.converter;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * 实体转换器基接口
 *
 * 所有 Entity ↔ DTO/VO 转换器必须继承本接口
 *
 * 【强制】使用 MapStruct，编译时自动生成转换代码，零运行时开销
 * 【强制】转换器命名规范：源实体+To+目标对象，如 CustomerToDTOConverter
 *
 * @param <S> Source 源实体类型
 * @param <T> Target 目标类型（DTO/VO）
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EntityConverter<S, T> {

    /**
     * 单个实体转换
     */
    T convert(S source);

    /**
     * 实体复制（同名属性拷贝）
     */
    S copy(S source, @MappingTarget S target);

    /**
     * 实体列表转换
     */
    List<T> convert(List<S> sources);

    /**
     * 空安全转换（source 为 null 时返回 null，不抛异常）
     */
    default T convertSafe(S source) {
        return source == null ? null : convert(source);
    }
}
