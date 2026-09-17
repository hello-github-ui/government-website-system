package com.gov.gows.mapper;

import com.gov.gows.entity.OperationLog;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 操作日志数据访问层。
 */
@Mapper
public interface OperationLogMapper {

    @Insert("INSERT INTO gov_operation_log(user_id,user_type,username,module,action,content,ip_address,user_agent) " +
            "VALUES(#{userId},#{userType},#{username},#{module},#{action},#{content},#{ipAddress},#{userAgent})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(OperationLog log);

    @Select("SELECT * FROM gov_operation_log ORDER BY id DESC LIMIT #{offset},#{size}")
    List<OperationLog> selectPage(@Param("offset") int offset, @Param("size") int size);

    @Select("SELECT COUNT(*) FROM gov_operation_log")
    long count();

    @Select("SELECT * FROM gov_operation_log WHERE id=#{id} LIMIT 1")
    OperationLog findById(@Param("id") Long id);

    @Delete("DELETE FROM gov_operation_log WHERE id=#{id}")
    int deleteById(@Param("id") Long id);

    @Delete("DELETE FROM gov_operation_log WHERE create_time < DATE_SUB(NOW(), INTERVAL #{days} DAY)")
    int clearOlderThan(@Param("days") int days);
}
