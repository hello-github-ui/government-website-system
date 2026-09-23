package com.example.gov.mapper;

import com.example.gov.entity.Consult;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 咨询投诉数据访问层。
 */
@Mapper
public interface ConsultMapper {

    @Select("SELECT * FROM gov_consult ORDER BY id DESC LIMIT #{offset},#{size}")
    List<Consult> selectPage(@Param("offset") int offset, @Param("size") int size);

    @Select("<script>SELECT * FROM gov_consult <where>" +
        "<if test='status != null'> AND status=#{status}</if></where> ORDER BY id DESC LIMIT #{offset},#{size}</script>")
    List<Consult> selectPageByStatus(@Param("status") Integer status, @Param("offset") int offset, @Param("size") int size);

    @Select("<script>SELECT COUNT(*) FROM gov_consult <where>" +
        "<if test='status != null'> AND status=#{status}</if></where></script>")
    long countByStatus(@Param("status") Integer status);

    @Select("SELECT * FROM gov_consult WHERE id=#{id} LIMIT 1")
    Consult findById(@Param("id") Long id);

    @Select("SELECT COUNT(*) FROM gov_consult WHERE status=0")
    long countPending();

    @Select("SELECT * FROM gov_consult WHERE status=2 AND is_public=1 ORDER BY reply_time DESC LIMIT #{offset},#{size}")
    List<Consult> selectPublicReplied(@Param("offset") int offset, @Param("size") int size);

    @Select("SELECT COUNT(*) FROM gov_consult WHERE status=2 AND is_public=1")
    long countPublicReplied();

    @Select("SELECT * FROM gov_consult WHERE user_id=#{userId} ORDER BY create_time DESC LIMIT #{offset},#{size}")
    List<Consult> selectByUser(@Param("userId") Long userId, @Param("offset") int offset, @Param("size") int size);

    @Select("SELECT COUNT(*) FROM gov_consult WHERE user_id=#{userId}")
    long countByUser(@Param("userId") Long userId);

    @Insert("INSERT INTO gov_consult(user_id,type,title,content,contact_name,contact_phone,contact_email,status,is_public,ip_address,create_time) " +
        "VALUES(#{userId},#{type},#{title},#{content},#{contactName},#{contactPhone},#{contactEmail},#{status},#{isPublic},#{ipAddress},NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Consult consult);

    @Update("UPDATE gov_consult SET status=1,update_time=NOW() WHERE id=#{id}")
    int markRead(@Param("id") Long id);

    @Update("UPDATE gov_consult SET reply_content=#{reply},reply_time=NOW(),reply_user_id=#{adminId},status=2,update_time=NOW() WHERE id=#{id}")
    int reply(@Param("id") Long id, @Param("reply") String reply, @Param("adminId") Long adminId);

    @Delete("DELETE FROM gov_consult WHERE id=#{id}")
    int deleteById(@Param("id") Long id);
}
