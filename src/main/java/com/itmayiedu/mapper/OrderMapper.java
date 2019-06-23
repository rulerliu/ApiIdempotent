package com.itmayiedu.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

import com.itmayiedu.entity.OrderEntity;

public interface OrderMapper {
	
	@Insert("insert order_info(order_name, order_desc) values (#{orderName}, #{orderDesc})")
	@Options(useGeneratedKeys = true, keyProperty = "orderId")
	public int addOrder(OrderEntity orderEntity);
}
