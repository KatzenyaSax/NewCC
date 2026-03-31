package com.dafuweng.system.service;

import com.dafuweng.common.core.result.PageResult;
import com.dafuweng.system.domain.dto.*;
import com.dafuweng.system.entity.SysDict;

import java.util.List;

public interface DictService {

    Long create(DictCreateRequest request);

    void update(DictUpdateRequest request);

    void delete(Long id);

    SysDict getById(Long id);

    List<DictVO> getByType(String dictType);

    PageResult<DictVO> page(DictPageRequest request);
}
