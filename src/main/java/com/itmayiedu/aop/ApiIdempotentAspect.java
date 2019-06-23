package com.itmayiedu.aop;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.itmayiedu.annotation.ExtApiIdempotent;
import com.itmayiedu.annotation.ExtApiToken;
import com.itmayiedu.utils.Constants;
import com.itmayiedu.utils.RedisTokenUtil;

@Aspect
@Component
public class ApiIdempotentAspect {
	@Autowired
	private RedisTokenUtil redisToken;

	// 1.使用AOP环绕通知拦截所有访问（controller）
	@Pointcut("execution(public * com.itmayiedu.controller.*.*(..))")
	public void rlAop() {
		
	}

	// 前置通知
	@Before("rlAop()")
	public void before(JoinPoint point) {
		Method method = getMethod(point);
		ExtApiToken extApiToken = method.getDeclaredAnnotation(ExtApiToken.class);
		if (extApiToken != null) {
			// 可以放入到AOP代码 前置通知，存放到页面的hidden
			getRequest().setAttribute("token", redisToken.getToken());
		}
	}

	// 环绕通知
	@Around("rlAop()")
	public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

		Method method = getMethod(proceedingJoinPoint);
		
		ExtApiIdempotent extApiIdempotent = method.getDeclaredAnnotation(ExtApiIdempotent.class);
		// 判断方法上是否有加ExtApiIdempotent
		if (extApiIdempotent != null) {
			String type = extApiIdempotent.type();
			HttpServletRequest request = getRequest();
			String token = null;
			if (Constants.EXT_API_HEAD.equals(type)) {
				token = request.getHeader("token");
			} else if (Constants.EXT_API_FORM.equals(type)) {
				token = request.getParameter("token");
			}
			
			if (StringUtils.isEmpty(token)) {
				return "参数错误...";
			}
			
			// 接口获取对应的令牌,如果能够获取该(从redis获取令牌)令牌(将当前令牌删除掉) 就直接执行该访问的业务逻辑
			boolean findToken = redisToken.findToken(token);
			// 接口获取对应的令牌,如果获取不到该令牌 直接返回请勿重复提交
			if (!findToken) {
				response("请勿重复提交!");
				return "fail";
			}
		}
		
		// 执行方法
		Object proceed = proceedingJoinPoint.proceed();
		return proceed;
	}
	
	private Method getMethod(JoinPoint proceedingJoinPoint) {
		MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
		// 获取方法
		Method method = methodSignature.getMethod();
		return method;
	}

	private HttpServletRequest getRequest() {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		return request;
	}

	private void response(String msg) throws IOException {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletResponse response = attributes.getResponse();
		response.setHeader("Content-type", "text/html; charset=UTF-8");
		PrintWriter writer = response.getWriter();
		try {
			writer.println(msg);
		} catch (Exception e) {

		} finally {
			writer.close();
		}

	}

}
