package com.dafuweng.system.service;

import com.dafuweng.common.core.result.PageResult;
import com.dafuweng.system.domain.dto.*;
import com.dafuweng.system.entity.SysDepartment;

import java.util.List;

public interface DepartmentService {

    Long create(DepartmentCreateRequest request);

    void update(DepartmentUpdateRequest request);

    void delete(Long id);

    SysDepartment getById(Long id);

    List<DepartmentTreeVO> getTree();

    List<DepartmentVO> getList();

    PageResult<DepartmentVO> page(DepartmentPageRequest request);
}
