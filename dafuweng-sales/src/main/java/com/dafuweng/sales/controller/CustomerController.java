package com.dafuweng.sales.controller;

import com.dafuweng.common.core.result.PageResult;
import com.dafuweng.common.core.result.R;
import com.dafuweng.sales.domain.dto.*;
import com.dafuweng.sales.domain.vo.CustomerPublicSeaVO;
import com.dafuweng.sales.domain.vo.CustomerVO;
import com.dafuweng.sales.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    /**
     * 客户查重（姓名+手机）
     */
    @PostMapping("/check")
    public R<CustomerDuplicateCheckResponse> checkDuplicate(@Valid @RequestBody CustomerDuplicateCheckRequest request) {
        return R.ok(customerService.checkDuplicate(request));
    }

    /**
     * 新增客户
     */
    @PostMapping
    public R<Long> createCustomer(@Valid @RequestBody CustomerCreateRequest request) {
        return R.ok(customerService.createCustomer(request));
    }

    /**
     * 客户详情
     */
    @GetMapping("/{id}")
    public R<CustomerVO> getCustomerById(@PathVariable Long id) {
        return R.ok(customerService.getCustomerById(id));
    }

    /**
     * 更新客户信息
     */
    @PutMapping("/{id}")
    public R<Void> updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerUpdateRequest request) {
        request.setId(id);
        customerService.updateCustomer(request);
        return R.ok();
    }

    /**
     * 删除客户（逻辑删除）
     */
    @DeleteMapping("/{id}")
    public R<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return R.ok();
    }

    /**
     * 分页查询客户
     */
    @GetMapping("/page")
    public R<PageResult<CustomerVO>> pageCustomer(@Valid CustomerPageRequest request) {
        return R.ok(customerService.pageCustomer(request));
    }

    /**
     * 客户迁移（部门经理操作）
     */
    @PutMapping("/{id}/transfer")
    public R<Void> transferCustomer(@PathVariable Long id, @Valid @RequestBody CustomerTransferRequest request) {
        request.setCustomerId(id);
        customerService.transferCustomer(request);
        return R.ok();
    }

    /**
     * 客户批注
     */
    @PutMapping("/{id}/annotate")
    public R<Void> annotateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerAnnotateRequest request) {
        request.setCustomerId(id);
        customerService.annotateCustomer(request);
        return R.ok();
    }

    /**
     * 公海客户列表
     */
    @GetMapping("/public-sea")
    public R<PageResult<CustomerPublicSeaVO>> getPublicSeaCustomers(@Valid CustomerPageRequest request) {
        return R.ok(customerService.getPublicSeaCustomers(request));
    }

    /**
     * 领取公海客户
     */
    @PutMapping("/{id}/claim")
    public R<Void> claimCustomer(@PathVariable Long id) {
        customerService.claimCustomer(id);
        return R.ok();
    }
}
