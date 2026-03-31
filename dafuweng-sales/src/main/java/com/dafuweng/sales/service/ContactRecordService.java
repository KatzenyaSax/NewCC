package com.dafuweng.sales.service;

import com.dafuweng.sales.domain.dto.ContactRecordCreateRequest;
import com.dafuweng.sales.domain.vo.ContactRecordVO;

import java.util.List;

public interface ContactRecordService {

    /**
     * 创建洽谈记录
     */
    Long createContactRecord(ContactRecordCreateRequest request);

    /**
     * 获取客户洽谈历史
     */
    List<ContactRecordVO> getCustomerContactHistory(Long customerId);
}
