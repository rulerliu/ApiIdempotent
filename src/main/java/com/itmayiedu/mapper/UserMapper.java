/**
 * 功能说明:
 * 功能作者:
 * 创建日期:
 * 版权归属:每特教育|蚂蚁课堂所有 www.itmayiedu.com
 */
package com.itmayiedu.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.itmayiedu.entity.UserEntity;

/**
 * 功能说明: <br>
 * 创建作者:每特教育-余胜军<br>
 * 创建时间:2018年7月10日 下午4:36:04<br>
 * 教育机构:每特教育|蚂蚁课堂<br>
 * 版权说明:上海每特教育科技有限公司版权所有<br>
 * 官方网站:www.itmayiedu.com|www.meitedu.com<br>
 * 联系方式:qq644064779<br>
 * 注意:本内容有每特教育学员共同研发,请尊重原创版权
 */
public interface UserMapper {

	@Select(" SELECT * FROM user_info where user_name = #{userName} and password = #{password}")
	public UserEntity login(UserEntity userEntity);

	@Insert("insert user_info(user_name, password) values (#{userName}, #{password})")
	public int insertUser(UserEntity userEntity);
	
}
