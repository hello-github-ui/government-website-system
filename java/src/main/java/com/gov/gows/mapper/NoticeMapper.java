package com.gov.gows.mapper;

import com.gov.gows.entity.Notice;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 公告数据访问层。
 */
@Mapper
public interface NoticeMapper {

    @Select("SELECT * FROM gov_notice ORDER BY id DESC LIMIT #{offset},#{size}")
    List<Notice> selectPage(@Param("offset") int offset, @Param("size") int size);

    @Select("SELECT COUNT(*) FROM gov_notice")
    long count();

    @Select("SELECT * FROM gov_notice WHERE status=1 ORDER BY is_top DESC, publish_time DESC LIMIT #{limit}")
    List<Notice> selectLatest(@Param("limit") int limit);

    @Select("SELECT * FROM gov_notice WHERE id=#{id} LIMIT 1")
    Notice findById(@Param("id") Long id);

    /** 前台分页列表：仅已发布 */
    @Select("SELECT * FROM gov_notice WHERE status=1 ORDER BY is_top DESC, publish_time DESC LIMIT #{offset},#{size}")
    List<Notice> selectPublishedPage(@Param("offset") int offset, @Param("size") int size);

    @Select("SELECT COUNT(*) FROM gov_notice WHERE status=1")
    long countPublished();

    @Update("UPDATE gov_notice SET views=views+1 WHERE id=#{id}")
    int increaseViews(@Param("id") Long id);

    @Insert("INSERT INTO gov_notice(title,summary,content,is_top,is_important,status,publish_time,create_time,update_time) " +
            "VALUES(#{title},#{summary},#{content},#{isTop},#{isImportant},#{status},NOW(),NOW(),NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Notice notice);

    @Update("<script>UPDATE gov_notice SET title=#{title},summary=#{summary},content=#{content}," +
            "is_top=#{isTop},is_important=#{isImportant},status=#{status},update_time=NOW()" +
            "<if test='publishTime != null'>,publish_time=#{publishTime}</if> WHERE id=#{id}</script>")
    int update(Notice notice);

    @Delete("DELETE FROM gov_notice WHERE id=#{id}")
    int deleteById(@Param("id") Long id);

    /** 关键词搜索（前台，仅已发布） */
    @Select("SELECT * FROM gov_notice WHERE status=1 AND (title LIKE CONCAT('%',#{kw},'%') OR content LIKE CONCAT('%',#{kw},'%') OR summary LIKE CONCAT('%',#{kw},'%')) " +
            "ORDER BY is_top DESC, publish_time DESC LIMIT #{offset},#{size}")
    List<Notice> search(@Param("kw") String kw, @Param("offset") int offset, @Param("size") int size);

    @Select("SELECT COUNT(*) FROM gov_notice WHERE status=1 AND (title LIKE CONCAT('%',#{kw},'%') OR content LIKE CONCAT('%',#{kw},'%') OR summary LIKE CONCAT('%',#{kw},'%'))")
    long searchCount(@Param("kw") String kw);
}
