package com.example.gov.mapper;

import com.example.gov.entity.Judicial;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 裁判文书数据访问层。
 */
@Mapper
public interface JudicialMapper {

    @Select("SELECT * FROM gov_judicial ORDER BY id DESC LIMIT #{offset},#{size}")
    List<Judicial> selectPage(@Param("offset") int offset, @Param("size") int size);

    @Select("SELECT COUNT(*) FROM gov_judicial")
    long count();

    @Select("SELECT * FROM gov_judicial WHERE id=#{id} LIMIT 1")
    Judicial findById(@Param("id") Long id);

    /**
     * 前台：公开且审核通过的列表
     */
    @Select("SELECT * FROM gov_judicial WHERE is_public=1 AND check_status=1 AND status=1 ORDER BY judge_date DESC LIMIT #{offset},#{size}")
    List<Judicial> selectPublicPage(@Param("offset") int offset, @Param("size") int size);

    @Select("SELECT COUNT(*) FROM gov_judicial WHERE is_public=1 AND check_status=1 AND status=1")
    long countPublic();

    @Update("UPDATE gov_judicial SET views=views+1 WHERE id=#{id}")
    int increaseViews(@Param("id") Long id);

    @Insert("INSERT INTO gov_judicial(case_no,case_name,court,case_type,content,judge_date,check_status,is_public,status,attachment,create_time,update_time) " +
        "VALUES(#{caseNo},#{caseName},#{court},#{caseType},#{content},#{judgeDate},#{checkStatus},#{isPublic},#{status},#{attachment},NOW(),NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Judicial judicial);

    @Update("<script>UPDATE gov_judicial SET case_no=#{caseNo},case_name=#{caseName},court=#{court},case_type=#{caseType}," +
        "content=#{content},judge_date=#{judgeDate},check_status=#{checkStatus},is_public=#{isPublic},status=#{status},attachment=#{attachment},update_time=NOW() WHERE id=#{id}</script>")
    int update(Judicial judicial);

    @Delete("DELETE FROM gov_judicial WHERE id=#{id}")
    int deleteById(@Param("id") Long id);

    /**
     * 前台全文检索
     */
    @Select("SELECT * FROM gov_judicial WHERE is_public=1 AND check_status=1 AND status=1 " +
        "AND (case_name LIKE CONCAT('%',#{kw},'%') OR case_no LIKE CONCAT('%',#{kw},'%') OR court LIKE CONCAT('%',#{kw},'%') OR content LIKE CONCAT('%',#{kw},'%')) " +
        "ORDER BY judge_date DESC LIMIT #{offset},#{size}")
    List<Judicial> search(@Param("kw") String kw, @Param("offset") int offset, @Param("size") int size);

    @Select("SELECT COUNT(*) FROM gov_judicial WHERE is_public=1 AND check_status=1 AND status=1 " +
        "AND (case_name LIKE CONCAT('%',#{kw},'%') OR case_no LIKE CONCAT('%',#{kw},'%') OR court LIKE CONCAT('%',#{kw},'%') OR content LIKE CONCAT('%',#{kw},'%'))")
    long searchCount(@Param("kw") String kw);
}
