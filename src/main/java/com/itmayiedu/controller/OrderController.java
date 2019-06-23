package com.itmayiedu.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itmayiedu.annotation.ExtApiIdempotent;
import com.itmayiedu.entity.OrderEntity;
import com.itmayiedu.mapper.OrderMapper;
import com.itmayiedu.utils.Constants;
import com.itmayiedu.utils.RedisTokenUtil;

@RestController
public class OrderController {

	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private RedisTokenUtil redisToken;
	
	@RequestMapping("/redisToken")
	public String redisToken() {
		return redisToken.getToken();
	}
	
	@RequestMapping(value = "/addOrder", produces = "application/json; charset=utf-8")
	public String addOrder(@RequestBody OrderEntity orderEntity, HttpServletRequest request) {
		String token = request.getHeader("token");
		if (StringUtils.isEmpty(token)) {
			return "参数错误...";
		}
		
		boolean findToken = redisToken.findToken(token);
		if (!findToken) {
			return "请勿重复提交...";
		}
		
		int orderId = orderMapper.addOrder(orderEntity);
		System.out.println("orderId:" + orderId);
		return orderId > 0 ? "添加成功..." : "添加失败...";
	}
	
	@ExtApiIdempotent(type = Constants.EXT_API_HEAD)
	@RequestMapping(value = "/addOrder2", produces = "application/json; charset=utf-8")
	public String addOrder2(@RequestBody OrderEntity orderEntity, HttpServletRequest request) {
		
		int orderId = orderMapper.addOrder(orderEntity);
		System.out.println("orderId:" + orderId);
		return orderId > 0 ? "添加成功..." : "添加失败...";
	}
	
}
