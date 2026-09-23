package com.example.gov.mapper;

import com.example.gov.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 前台用户数据访问层。
 */
@Mapper
public interface UserMapper {

    @Select("SELECT * FROM gov_user WHERE username = #{username} LIMIT 1")
    User findByUsername(@Param("username") String username);

    @Select("SELECT * FROM gov_user WHERE email = #{email} LIMIT 1")
    User findByEmail(@Param("email") String email);

    @Select("SELECT * FROM gov_user WHERE id = #{id} LIMIT 1")
    User findById(@Param("id") Long id);

    @Select("SELECT * FROM gov_user ORDER BY id DESC LIMIT #{offset},#{size}")
    List<User> selectPage(@Param("offset") int offset, @Param("size") int size);

    @Select("SELECT COUNT(*) FROM gov_user")
    long count();

    @Insert("INSERT INTO gov_user(username,password,real_name,phone,email,status) " +
        "VALUES(#{username},#{password},#{realName},#{phone},#{email},#{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Update("UPDATE gov_user SET real_name=#{realName},email=#{email},phone=#{phone},status=#{status} WHERE id=#{id}")
    int update(User user);

    @Update("UPDATE gov_user SET password=#{password} WHERE id=#{id}")
    int updatePassword(@Param("id") Long id, @Param("password") String password);

    @Update("UPDATE gov_user SET last_login_time=NOW(),last_login_ip=#{ip},login_fail_count=0,lock_time=NULL WHERE id=#{id}")
    int updateLoginInfo(@Param("id") Long id, @Param("ip") String ip);

    @Update("UPDATE gov_user SET login_fail_count=#{failCount},lock_time=#{lockTime} WHERE id=#{id}")
    int updateFailCount(@Param("id") Long id, @Param("failCount") int failCount, @Param("lockTime") String lockTime);

    @Delete("DELETE FROM gov_user WHERE id=#{id}")
    int deleteById(@Param("id") Long id);
}
