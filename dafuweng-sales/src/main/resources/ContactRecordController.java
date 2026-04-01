package com.dafuweng.sales.controller;

import com.dafuweng.common.core.result.R;
import com.dafuweng.sales.domain.dto.ContactRecordCreateRequest;
import com.dafuweng.sales.domain.vo.ContactRecordVO;
import com.dafuweng.sales.service.ContactRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contact-record")
@RequiredArgsConstructor
public class ContactRecordController {

    private final ContactRecordService contactRecordService;

    /**
     * 新增洽谈记录
     */
    @PostMapping
    public R<Void> createContactRecord(@Valid @RequestBody ContactRecordCreateRequest request) {
        contactRecordService.createContactRecord(request);
        return R.ok();
    }

    /**
     * 查看客户洽谈历史
     */
    @GetMapping("/customer/{customerId}")
    public R<List<ContactRecordVO>> getCustomerContactHistory(@PathVariable Long customerId) {
        return R.ok(contactRecordService.getCustomerContactHistory(customerId));
    }
}
