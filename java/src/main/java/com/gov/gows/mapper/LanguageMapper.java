package com.gov.gows.mapper;

import com.gov.gows.entity.Language;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 前端文字配置数据访问层。
 */
@Mapper
public interface LanguageMapper {

    @Select("SELECT * FROM gov_language WHERE lang_group=#{group} AND module=#{module} ORDER BY id ASC")
    List<Language> selectByGroup(@Param("group") String group, @Param("module") String module);

    @Select("SELECT * FROM gov_language WHERE module=#{module} ORDER BY id ASC")
    List<Language> selectByModule(@Param("module") String module);

    @Select("SELECT * FROM gov_language ORDER BY id ASC LIMIT #{offset},#{size}")
    List<Language> selectPage(@Param("offset") int offset, @Param("size") int size);

    @Select("SELECT COUNT(*) FROM gov_language")
    long count();

    @Select("SELECT * FROM gov_language WHERE id=#{id} LIMIT 1")
    Language findById(@Param("id") Long id);

    @Insert("INSERT INTO gov_language(lang_key,lang_value,lang_group,module,is_default) " +
            "VALUES(#{langKey},#{langValue},#{langGroup},#{module},#{isDefault})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Language language);

    @Update("UPDATE gov_language SET lang_value=#{langValue},lang_group=#{langGroup},module=#{module},is_default=#{isDefault} WHERE id=#{id}")
    int update(Language language);

    @Update("UPDATE gov_language SET lang_value=#{value} WHERE lang_key=#{key}")
    int updateValueByKey(@Param("key") String key, @Param("value") String value);

    @Delete("DELETE FROM gov_language WHERE id=#{id}")
    int deleteById(@Param("id") Long id);
}
