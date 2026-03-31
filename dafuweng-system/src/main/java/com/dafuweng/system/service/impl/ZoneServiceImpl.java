package com.dafuweng.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dafuweng.common.core.enums.ErrorCode;
import com.dafuweng.common.core.exception.BusinessException;
import com.dafuweng.common.core.result.PageResult;
import com.dafuweng.system.converter.ZoneConverter;
import com.dafuweng.system.domain.dto.*;
import com.dafuweng.system.entity.SysZone;
import com.dafuweng.system.mapper.SysZoneMapper;
import com.dafuweng.system.service.ZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ZoneServiceImpl implements ZoneService {

    private final SysZoneMapper zoneMapper;
    private final ZoneConverter zoneConverter;

    @Override
    @Transactional
    public Long create(ZoneCreateRequest request) {
        // Check if zoneCode already exists
        LambdaQueryWrapper<SysZone> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysZone::getZoneCode, request.getZoneCode());
        if (zoneMapper.exists(wrapper)) {
            throw new BusinessException(ErrorCode.DATA_CONFLICT);
        }

        SysZone zone = zoneConverter.toEntity(request);
        zoneMapper.insert(zone);
        return zone.getId();
    }

    @Override
    @Transactional
    public void update(ZoneUpdateRequest request) {
        SysZone zone = zoneMapper.selectById(request.getId());
        if (zone == null) {
            throw new BusinessException(ErrorCode.ZONE_NOT_FOUND);
        }

        zoneConverter.copy(request, zone);
        zoneMapper.updateById(zone);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        zoneMapper.deleteById(id);
    }

    @Override
    public SysZone getById(Long id) {
        SysZone zone = zoneMapper.selectById(id);
        if (zone == null) {
            throw new BusinessException(ErrorCode.ZONE_NOT_FOUND);
        }
        return zone;
    }

    @Override
    public List<ZoneVO> getList() {
        LambdaQueryWrapper<SysZone> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(SysZone::getSortOrder);
        List<SysZone> zones = zoneMapper.selectList(wrapper);
        return zoneConverter.toVOList(zones);
    }

    @Override
    public PageResult<ZoneVO> page(ZonePageRequest request) {
        Page<SysZone> page = new Page<>(request.getPageNum(), request.getPageSize());
        LambdaQueryWrapper<SysZone> wrapper = new LambdaQueryWrapper<>();
        if (request.getStatus() != null) {
            wrapper.eq(SysZone::getStatus, request.getStatus());
        }
        wrapper.orderByAsc(SysZone::getSortOrder);
        IPage<SysZone> result = zoneMapper.selectPage(page, wrapper);
        return PageResult.of(result);
    }
}
