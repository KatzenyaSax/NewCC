package com.dafuweng.sales.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dafuweng.common.core.enums.ErrorCode;
import com.dafuweng.common.core.exception.BusinessException;
import com.dafuweng.common.core.result.PageResult;
import com.dafuweng.sales.converter.CustomerConverter;
import com.dafuweng.sales.domain.dto.*;
import com.dafuweng.sales.domain.vo.CustomerPublicSeaVO;
import com.dafuweng.sales.domain.vo.CustomerVO;
import com.dafuweng.sales.entity.Customer;
import com.dafuweng.sales.enums.CustomerStatus;
import com.dafuweng.sales.mapper.CustomerMapper;
import com.dafuweng.sales.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerMapper customerMapper;
    private final CustomerConverter customerConverter;

    @Override
    public CustomerDuplicateCheckResponse checkDuplicate(CustomerDuplicateCheckRequest request) {
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Customer::getName, request.getName())
               .eq(Customer::getPhone, request.getPhone())
               .eq(Customer::getDeleted, 0);

        Customer existCustomer = customerMapper.selectOne(wrapper);

        CustomerDuplicateCheckResponse response = new CustomerDuplicateCheckResponse();
        if (existCustomer != null) {
            response.setDuplicate(true);
            response.setExistCustomerId(existCustomer.getId());
            response.setExistCustomerName(existCustomer.getName());
            response.setExistCreatedAt(existCustomer.getCreatedAt());
        } else {
            response.setDuplicate(false);
        }
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createCustomer(CustomerCreateRequest request) {
        Customer customer = customerConverter.toEntity(request);
        // TODO: 设置salesRepId, deptId, zoneId, createdBy等字段（从SecurityContext获取）
        customer.setStatus(CustomerStatus.POTENTIAL.getCode());
        customerMapper.insert(customer);
        return customer.getId();
    }

    @Override
    public CustomerVO getCustomerById(Long id) {
        Customer customer = customerMapper.selectById(id);
        if (customer == null) {
            throw new BusinessException(ErrorCode.CUSTOMER_NOT_FOUND);
        }
        return customerConverter.toVO(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCustomer(CustomerUpdateRequest request) {
        Customer existCustomer = customerMapper.selectById(request.getId());
        if (existCustomer == null) {
            throw new BusinessException(ErrorCode.CUSTOMER_NOT_FOUND);
        }

        Customer customer = customerConverter.toEntity(request);
        customerMapper.updateById(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCustomer(Long id) {
        Customer customer = customerMapper.selectById(id);
        if (customer == null) {
            throw new BusinessException(ErrorCode.CUSTOMER_NOT_FOUND);
        }
        customerMapper.deleteById(id);
    }

    @Override
    public PageResult<CustomerVO> pageCustomer(CustomerPageRequest request) {
        Page<Customer> page = new Page<>(request.getPageNum(), request.getPageSize());

        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
        if (request.getName() != null) {
            wrapper.like(Customer::getName, request.getName());
        }
        if (request.getPhone() != null) {
            wrapper.eq(Customer::getPhone, request.getPhone());
        }
        if (request.getCustomerType() != null) {
            wrapper.eq(Customer::getCustomerType, request.getCustomerType());
        }
        if (request.getStatus() != null) {
            wrapper.eq(Customer::getStatus, request.getStatus());
        }
        if (request.getIntentionLevel() != null) {
            wrapper.eq(Customer::getIntentionLevel, request.getIntentionLevel());
        }
        if (request.getSalesRepId() != null) {
            wrapper.eq(Customer::getSalesRepId, request.getSalesRepId());
        }
        if (request.getDeptId() != null) {
            wrapper.eq(Customer::getDeptId, request.getDeptId());
        }
        if (request.getZoneId() != null) {
            wrapper.eq(Customer::getZoneId, request.getZoneId());
        }

        wrapper.eq(Customer::getDeleted, 0);
        wrapper.orderByDesc(Customer::getCreatedAt);

        IPage<Customer> pageResult = customerMapper.selectPage(page, wrapper);
        return PageResult.of(pageResult);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transferCustomer(CustomerTransferRequest request) {
        Customer customer = customerMapper.selectById(request.getCustomerId());
        if (customer == null) {
            throw new BusinessException(ErrorCode.CUSTOMER_NOT_FOUND);
        }
        // TODO: 检查权限，转移客户
        customer.setSalesRepId(request.getToSalesRepId());
        customerMapper.updateById(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void annotateCustomer(CustomerAnnotateRequest request) {
        Customer customer = customerMapper.selectById(request.getCustomerId());
        if (customer == null) {
            throw new BusinessException(ErrorCode.CUSTOMER_NOT_FOUND);
        }
        // TODO: 追加批注到annotation字段
    }

    @Override
    public PageResult<CustomerPublicSeaVO> getPublicSeaCustomers(CustomerPageRequest request) {
        Page<Customer> page = new Page<>(request.getPageNum(), request.getPageSize());

        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Customer::getStatus, CustomerStatus.PUBLIC_SEA.getCode())
               .eq(Customer::getDeleted, 0)
               .orderByDesc(Customer::getPublicSeaTime);

        IPage<Customer> pageResult = customerMapper.selectPage(page, wrapper);

        PageResult<Customer> result = PageResult.of(pageResult);
        // TODO: 转换为CustomerPublicSeaVO
        return PageResult.empty(request.getPageSize());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void claimCustomer(Long customerId) {
        Customer customer = customerMapper.selectById(customerId);
        if (customer == null) {
            throw new BusinessException(ErrorCode.CUSTOMER_NOT_FOUND);
        }
        if (customer.getStatus() != CustomerStatus.PUBLIC_SEA.getCode()) {
            throw new BusinessException(ErrorCode.CUSTOMER_CLAIM_FORBIDDEN);
        }
        // TODO: 从SecurityContext获取当前用户，设置为salesRepId，状态改为POTENTIAL
    }
}
