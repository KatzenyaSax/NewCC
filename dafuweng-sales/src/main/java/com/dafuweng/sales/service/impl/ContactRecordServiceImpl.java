package com.dafuweng.sales.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dafuweng.common.core.enums.ErrorCode;
import com.dafuweng.common.core.exception.BusinessException;
import com.dafuweng.sales.converter.ContactRecordConverter;
import com.dafuweng.sales.domain.dto.ContactRecordCreateRequest;
import com.dafuweng.sales.domain.vo.ContactRecordVO;
import com.dafuweng.sales.entity.ContactRecord;
import com.dafuweng.sales.entity.Customer;
import com.dafuweng.sales.mapper.ContactRecordMapper;
import com.dafuweng.sales.mapper.CustomerMapper;
import com.dafuweng.sales.service.ContactRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactRecordServiceImpl implements ContactRecordService {

    private final ContactRecordMapper contactRecordMapper;
    private final CustomerMapper customerMapper;
    private final ContactRecordConverter contactRecordConverter;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createContactRecord(ContactRecordCreateRequest request) {
        // 验证客户存在
        Customer customer = customerMapper.selectById(request.getCustomerId());
        if (customer == null) {
            throw new BusinessException(ErrorCode.CUSTOMER_NOT_FOUND);
        }

        ContactRecord contactRecord = contactRecordConverter.toEntity(request);
        // TODO: 设置salesRepId, intentionAfter等字段
        contactRecordMapper.insert(contactRecord);
        return contactRecord.getId();
    }

    @Override
    public List<ContactRecordVO> getCustomerContactHistory(Long customerId) {
        LambdaQueryWrapper<ContactRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ContactRecord::getCustomerId, customerId)
               .eq(ContactRecord::getDeleted, 0)
               .orderByDesc(ContactRecord::getContactDate);

        List<ContactRecord> records = contactRecordMapper.selectList(wrapper);
        return contactRecordConverter.toVOList(records);
    }
}
