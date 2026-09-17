package com.gov.gows.mapper;

import com.gov.gows.entity.LoginLog;
import org.apache.ibatis.annotations.*;

/**
 * 登录日志数据访问层。
 */
@Mapper
public interface LoginLogMapper {

    @Insert("INSERT INTO gov_login_log(user_id,user_type,username,login_type,login_status,fail_reason,ip_address,user_agent) " +
            "VALUES(#{userId},#{userType},#{username},#{loginType},#{loginStatus},#{failReason},#{ipAddress},#{userAgent})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(LoginLog log);
}
