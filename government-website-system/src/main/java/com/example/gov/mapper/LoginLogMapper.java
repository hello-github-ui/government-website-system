package com.example.gov.mapper;

import com.example.gov.entity.LoginLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 登录日志数据访问层。
 */
@Mapper
public interface LoginLogMapper {

    @Insert("INSERT INTO gov_login_log(user_id,user_type,username,login_type,login_status,fail_reason,ip_address,user_agent) " +
        "VALUES(#{userId},#{userType},#{username},#{loginType},#{loginStatus},#{failReason},#{ipAddress},#{userAgent})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(LoginLog log);

    /**
     * 分页查询登录日志（可按登录状态筛选）。
     */
    @Select("<script>SELECT * FROM gov_login_log" +
        "<where><if test='loginStatus != null'>login_status=#{loginStatus}</if></where>" +
        " ORDER BY id DESC LIMIT #{offset},#{size}</script>")
    List<LoginLog> selectPage(@Param("loginStatus") Integer loginStatus,
                              @Param("offset") int offset, @Param("size") int size);

    /**
     * 统计登录日志条数。
     */
    @Select("<script>SELECT COUNT(*) FROM gov_login_log" +
        "<where><if test='loginStatus != null'>login_status=#{loginStatus}</if></where></script>")
    long count(@Param("loginStatus") Integer loginStatus);
}
