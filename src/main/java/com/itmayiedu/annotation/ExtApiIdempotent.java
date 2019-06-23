package com.itmayiedu.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//解决接口幂等性 支持网络延迟和表单重复提交
@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExtApiIdempotent {

	/** token放在请求头还是请求体 */
	String type();
	
}
