package com.dafuweng.sales.service;

import com.dafuweng.common.core.result.PageResult;
import com.dafuweng.sales.domain.dto.*;
import com.dafuweng.sales.domain.vo.CustomerPublicSeaVO;
import com.dafuweng.sales.domain.vo.CustomerVO;

public interface CustomerService {

    /**
     * 客户查重
     */
    CustomerDuplicateCheckResponse checkDuplicate(CustomerDuplicateCheckRequest request);

    /**
     * 创建客户
     */
    Long createCustomer(CustomerCreateRequest request);

    /**
     * 客户详情
     */
    CustomerVO getCustomerById(Long id);

    /**
     * 更新客户
     */
    void updateCustomer(CustomerUpdateRequest request);

    /**
     * 删除客户
     */
    void deleteCustomer(Long id);

    /**
     * 分页查询客户
     */
    PageResult<CustomerVO> pageCustomer(CustomerPageRequest request);

    /**
     * 客户迁移
     */
    void transferCustomer(CustomerTransferRequest request);

    /**
     * 客户批注
     */
    void annotateCustomer(CustomerAnnotateRequest request);

    /**
     * 公海客户列表
     */
    PageResult<CustomerPublicSeaVO> getPublicSeaCustomers(CustomerPageRequest request);

    /**
     * 领取公海客户
     */
    void claimCustomer(Long customerId);
}
