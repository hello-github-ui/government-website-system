package com.example.gov.mapper;

import com.example.gov.entity.Policy;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 政策法规数据访问层。
 */
@Mapper
public interface PolicyMapper {

    @Select("SELECT * FROM gov_policy ORDER BY id DESC LIMIT #{offset},#{size}")
    List<Policy> selectPage(@Param("offset") int offset, @Param("size") int size);

    @Select("SELECT COUNT(*) FROM gov_policy")
    long count();

    @Select("SELECT * FROM gov_policy WHERE status=1 ORDER BY publish_date DESC LIMIT #{limit}")
    List<Policy> selectLatest(@Param("limit") int limit);

    @Select("SELECT * FROM gov_policy WHERE id=#{id} LIMIT 1")
    Policy findById(@Param("id") Long id);

    @Select("SELECT * FROM gov_policy WHERE status=1 ORDER BY publish_date DESC LIMIT #{offset},#{size}")
    List<Policy> selectPublishedPage(@Param("offset") int offset, @Param("size") int size);

    @Select("SELECT COUNT(*) FROM gov_policy WHERE status=1")
    long countPublished();

    @Insert("INSERT INTO gov_policy(title,publish_org,content,category_id,status,publish_date,attachment,create_time,update_time) " +
        "VALUES(#{title},#{publishOrg},#{content},#{categoryId},#{status},#{publishDate},#{attachment},NOW(),NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Policy policy);

    @Update("<script>UPDATE gov_policy SET title=#{title},publish_org=#{publishOrg},content=#{content}," +
        "category_id=#{categoryId},status=#{status},publish_date=#{publishDate},attachment=#{attachment},update_time=NOW() WHERE id=#{id}</script>")
    int update(Policy policy);

    @Delete("DELETE FROM gov_policy WHERE id=#{id}")
    int deleteById(@Param("id") Long id);

    @Select("SELECT * FROM gov_policy WHERE status=1 AND (title LIKE CONCAT('%',#{kw},'%') OR content LIKE CONCAT('%',#{kw},'%') OR publish_org LIKE CONCAT('%',#{kw},'%')) " +
        "ORDER BY publish_date DESC LIMIT #{offset},#{size}")
    List<Policy> search(@Param("kw") String kw, @Param("offset") int offset, @Param("size") int size);

    @Select("SELECT COUNT(*) FROM gov_policy WHERE status=1 AND (title LIKE CONCAT('%',#{kw},'%') OR content LIKE CONCAT('%',#{kw},'%') OR publish_org LIKE CONCAT('%',#{kw},'%'))")
    long searchCount(@Param("kw") String kw);
}
