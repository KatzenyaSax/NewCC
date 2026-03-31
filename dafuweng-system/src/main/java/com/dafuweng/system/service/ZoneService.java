package com.dafuweng.system.service;

import com.dafuweng.common.core.result.PageResult;
import com.dafuweng.system.domain.dto.*;
import com.dafuweng.system.entity.SysZone;

import java.util.List;

public interface ZoneService {

    Long create(ZoneCreateRequest request);

    void update(ZoneUpdateRequest request);

    void delete(Long id);

    SysZone getById(Long id);

    List<ZoneVO> getList();

    PageResult<ZoneVO> page(ZonePageRequest request);
}
