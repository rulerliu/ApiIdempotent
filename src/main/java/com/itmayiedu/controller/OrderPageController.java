package com.itmayiedu.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itmayiedu.annotation.ExtApiIdempotent;
import com.itmayiedu.annotation.ExtApiToken;
import com.itmayiedu.entity.OrderEntity;
import com.itmayiedu.mapper.OrderMapper;
import com.itmayiedu.utils.Constants;
import com.itmayiedu.utils.RedisTokenUtil;

@Controller
public class OrderPageController {

	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private RedisTokenUtil redisToken;
	
	@RequestMapping("/indexPage")
	@ExtApiToken
	public String indexPage(HttpServletRequest request) {
		// 放到aop的前置通知
		// request.setAttribute("token", redisToken.getToken());
		return "indexPage";
	}
	
	@RequestMapping("/addOrderPage")
	@ExtApiIdempotent(type = Constants.EXT_API_FORM)
	public String addOrderPage(OrderEntity orderEntity, Model model) {
		int orderId = orderMapper.addOrder(orderEntity);
		System.out.println("orderId:" + orderId);
		model.addAttribute("order", orderEntity);
		return orderId > 0 ? "success" : "fail";
	}
	
	
	@RequestMapping("/index")
	public String index(HttpServletRequest request) {
		return "redirect:indexPage";
	}
	
	@RequestMapping("/customRedirection")
	public void customRedirection(HttpServletResponse response) {
		// 重定向到indexPage.jsp
		response.setStatus(302);
		response.setHeader("location", "indexPage");
	}
	
}
