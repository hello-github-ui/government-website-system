package com.example.gov.mapper;

import com.example.gov.entity.Nav;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 导航菜单数据访问层。
 */
@Mapper
public interface NavMapper {

    @Select("SELECT * FROM gov_nav ORDER BY sort ASC, id ASC")
    List<Nav> selectAll();

    @Select("SELECT * FROM gov_nav WHERE parent_id=#{parentId} ORDER BY sort ASC")
    List<Nav> selectByParent(@Param("parentId") Long parentId);

    @Select("SELECT * FROM gov_nav WHERE id=#{id} LIMIT 1")
    Nav findById(@Param("id") Long id);

    @Insert("INSERT INTO gov_nav(parent_id,nav_name,nav_url,nav_type,target,icon,sort,is_show) " +
        "VALUES(#{parentId},#{navName},#{navUrl},#{navType},#{target},#{icon},#{sort},#{isShow})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Nav nav);

    @Update("UPDATE gov_nav SET parent_id=#{parentId},nav_name=#{navName},nav_url=#{navUrl},nav_type=#{navType}," +
        "target=#{target},icon=#{icon},sort=#{sort},is_show=#{isShow} WHERE id=#{id}")
    int update(Nav nav);

    @Update("UPDATE gov_nav SET sort=#{sort} WHERE id=#{id}")
    int updateSort(@Param("id") Long id, @Param("sort") int sort);

    @Select("SELECT COUNT(*) FROM gov_nav WHERE parent_id=#{parentId}")
    long countChildren(@Param("parentId") Long parentId);

    @Delete("DELETE FROM gov_nav WHERE id=#{id}")
    int deleteById(@Param("id") Long id);
}
