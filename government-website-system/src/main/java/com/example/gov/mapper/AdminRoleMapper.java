package com.example.gov.mapper;

import com.example.gov.entity.AdminRole;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 管理员角色数据访问层。
 */
@Mapper
public interface AdminRoleMapper {

    @Select("SELECT * FROM gov_admin_role ORDER BY sort ASC, id ASC")
    List<AdminRole> selectAll();

    @Select("SELECT * FROM gov_admin_role ORDER BY id ASC LIMIT #{offset},#{size}")
    List<AdminRole> selectPage(@Param("offset") int offset, @Param("size") int size);

    @Select("SELECT COUNT(*) FROM gov_admin_role")
    long count();

    @Select("SELECT * FROM gov_admin_role WHERE id=#{id} LIMIT 1")
    AdminRole findById(@Param("id") Long id);

    @Select("SELECT COUNT(*) FROM gov_admin WHERE role_id=#{roleId}")
    long countAdminsByRole(@Param("roleId") Long roleId);

    @Insert("INSERT INTO gov_admin_role(role_name,role_desc,permissions,sort,status) " +
        "VALUES(#{roleName},#{roleDesc},#{permissions},#{sort},#{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(AdminRole role);

    @Update("UPDATE gov_admin_role SET role_name=#{roleName},role_desc=#{roleDesc},permissions=#{permissions},sort=#{sort},status=#{status} WHERE id=#{id}")
    int update(AdminRole role);

    @Delete("DELETE FROM gov_admin_role WHERE id=#{id}")
    int deleteById(@Param("id") Long id);
}
