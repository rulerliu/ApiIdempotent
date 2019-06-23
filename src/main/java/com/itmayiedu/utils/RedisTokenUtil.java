package com.itmayiedu.utils;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

// 如何生成Toke
@Component
public class RedisTokenUtil {

	@Autowired
	private RedisUtil baseRedisService;

	/**
	 * 生成token，放入redis，
	 * key：token，value：token
	 * @return
	 */
	public String getToken() {
		// 生成token，保证临时且唯一，
		String token = "token_" + UUID.randomUUID();
		// 放入redis中，保证临时
		baseRedisService.setString(token, token, 60 * 60L);
		System.out.println("token:" + token);
		return token;
	}

	// 如何使用Token 解决幂等性
	// 1.在调用接口之前生成对应的token，存放在redis中
	// 2.在调用接口的时候，将该令牌放入请求头中
	// 3.接口获取对应的token，如果获取到了该token，删除该token，执行该方法的逻辑，
	// 如果获取不到该token，直接返回请勿重新提交提示

	public boolean findToken(String tokenKey) {
		String tokenValue = (String) baseRedisService.getString(tokenKey);
		if (StringUtils.isEmpty(tokenValue)) {
			return false;
		}
		// 保证每个接口的token，只能被访问一次，保证接口幂等性问题，第二次直接返回false
		baseRedisService.delKey(tokenKey);
		return true;
	}

}
