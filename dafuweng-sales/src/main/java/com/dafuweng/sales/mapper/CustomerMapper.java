package com.dafuweng.sales.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dafuweng.sales.entity.Customer;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {
}
