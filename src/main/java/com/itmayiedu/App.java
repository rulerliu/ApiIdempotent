package com.itmayiedu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 0048-(每特教育&每特学院&蚂蚁课堂)-3期-互联网安全架构-纯手互联网API接口幂等框架
* <p>Title: App</p>  
* <p>Description:  </p>  
* @author liuwq  
* @date 2019年1月5日  下午6:18:49
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.itmayiedu.mapper"})
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
	
}
