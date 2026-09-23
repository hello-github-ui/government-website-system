package com.example.gov.mapper;

import com.example.gov.entity.Admin;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 管理员数据访问层。
 */
@Mapper
public interface AdminMapper {

    @Select("SELECT * FROM gov_admin WHERE username = #{username} LIMIT 1")
    Admin findByUsername(@Param("username") String username);

    @Select("SELECT * FROM gov_admin WHERE id = #{id} LIMIT 1")
    Admin findById(@Param("id") Long id);

    @Select("SELECT * FROM gov_admin ORDER BY id DESC LIMIT #{offset},#{size}")
    List<Admin> selectPage(@Param("offset") int offset, @Param("size") int size);

    @Select("SELECT COUNT(*) FROM gov_admin")
    long count();

    @Insert("INSERT INTO gov_admin(username,password,name,phone,email,role_id,is_super,is_admin,auth_status,status) " +
        "VALUES(#{username},#{password},#{name},#{phone},#{email},#{roleId},#{isSuper},#{isAdmin},#{authStatus},#{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Admin admin);

    @Update("UPDATE gov_admin SET name=#{name},email=#{email},phone=#{phone},role_id=#{roleId}," +
        "is_super=#{isSuper},status=#{status} WHERE id=#{id}")
    int update(Admin admin);

    @Update("UPDATE gov_admin SET password=#{password} WHERE id=#{id}")
    int updatePassword(@Param("id") Long id, @Param("password") String password);

    @Update("UPDATE gov_admin SET last_login_time=NOW(),last_login_ip=#{ip},login_fail_count=0,lock_time=NULL WHERE id=#{id}")
    int updateLoginInfo(@Param("id") Long id, @Param("ip") String ip);

    @Update("UPDATE gov_admin SET login_fail_count=#{failCount},lock_time=#{lockTime} WHERE id=#{id}")
    int updateFailCount(@Param("id") Long id, @Param("failCount") int failCount, @Param("lockTime") String lockTime);

    @Delete("DELETE FROM gov_admin WHERE id=#{id}")
    int deleteById(@Param("id") Long id);

    @Select("SELECT permissions FROM gov_admin_role WHERE id=#{roleId} AND status=1 LIMIT 1")
    String getRolePermissions(@Param("roleId") Long roleId);
}
