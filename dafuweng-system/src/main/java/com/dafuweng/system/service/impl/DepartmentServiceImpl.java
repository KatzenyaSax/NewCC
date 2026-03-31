package com.dafuweng.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dafuweng.common.core.enums.ErrorCode;
import com.dafuweng.common.core.exception.BusinessException;
import com.dafuweng.common.core.result.PageResult;
import com.dafuweng.system.converter.DepartmentConverter;
import com.dafuweng.system.domain.dto.*;
import com.dafuweng.system.entity.SysDepartment;
import com.dafuweng.system.mapper.SysDepartmentMapper;
import com.dafuweng.system.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final SysDepartmentMapper departmentMapper;
    private final DepartmentConverter departmentConverter;

    @Override
    @Transactional
    public Long create(DepartmentCreateRequest request) {
        // Check if deptCode already exists
        LambdaQueryWrapper<SysDepartment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDepartment::getDeptCode, request.getDeptCode());
        if (departmentMapper.exists(wrapper)) {
            throw new BusinessException(ErrorCode.DATA_CONFLICT);
        }

        SysDepartment department = departmentConverter.toEntity(request);
        departmentMapper.insert(department);
        return department.getId();
    }

    @Override
    @Transactional
    public void update(DepartmentUpdateRequest request) {
        SysDepartment department = departmentMapper.selectById(request.getId());
        if (department == null) {
            throw new BusinessException(ErrorCode.DEPT_NOT_FOUND);
        }

        departmentConverter.copy(request, department);
        departmentMapper.updateById(department);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // Check if department has children
        LambdaQueryWrapper<SysDepartment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDepartment::getParentId, id);
        if (departmentMapper.exists(wrapper)) {
            throw new BusinessException(ErrorCode.DEPT_EXISTS_CHILDREN);
        }

        departmentMapper.deleteById(id);
    }

    @Override
    public SysDepartment getById(Long id) {
        SysDepartment department = departmentMapper.selectById(id);
        if (department == null) {
            throw new BusinessException(ErrorCode.DEPT_NOT_FOUND);
        }
        return department;
    }

    @Override
    public List<DepartmentTreeVO> getTree() {
        LambdaQueryWrapper<SysDepartment> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(SysDepartment::getSortOrder);
        List<SysDepartment> departments = departmentMapper.selectList(wrapper);
        return buildTree(departments, 0L);
    }

    @Override
    public List<DepartmentVO> getList() {
        LambdaQueryWrapper<SysDepartment> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(SysDepartment::getSortOrder);
        List<SysDepartment> departments = departmentMapper.selectList(wrapper);
        return departmentConverter.toVOList(departments);
    }

    @Override
    public PageResult<DepartmentVO> page(DepartmentPageRequest request) {
        Page<SysDepartment> page = new Page<>(request.getPageNum(), request.getPageSize());
        LambdaQueryWrapper<SysDepartment> wrapper = new LambdaQueryWrapper<>();
        if (request.getZoneId() != null) {
            wrapper.eq(SysDepartment::getZoneId, request.getZoneId());
        }
        if (request.getStatus() != null) {
            wrapper.eq(SysDepartment::getStatus, request.getStatus());
        }
        wrapper.orderByAsc(SysDepartment::getSortOrder);
        IPage<SysDepartment> result = departmentMapper.selectPage(page, wrapper);
        return PageResult.of(result);
    }

    private List<DepartmentTreeVO> buildTree(List<SysDepartment> departments, Long parentId) {
        return departments.stream()
                .filter(d -> d.getParentId().equals(parentId))
                .map(d -> {
                    DepartmentTreeVO vo = departmentConverter.toTreeVO(d);
                    vo.setChildren(buildTree(departments, d.getId()));
                    return vo;
                })
                .toList();
    }
}
