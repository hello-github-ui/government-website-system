package com.gov.gows.mapper;

import com.gov.gows.entity.Setting;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 网站设置数据访问层。
 */
@Mapper
public interface SettingMapper {

    @Select("SELECT * FROM gov_settings")
    List<Setting> selectAll();

    @Select("SELECT * FROM gov_settings WHERE setting_key=#{key} LIMIT 1")
    Setting findByKey(@Param("key") String key);

    @Insert("INSERT INTO gov_settings(setting_key,setting_value,setting_group,setting_desc) " +
            "VALUES(#{settingKey},#{settingValue},#{settingGroup},#{settingDesc})")
    int insert(Setting setting);

    @Update("UPDATE gov_settings SET setting_value=#{value} WHERE setting_key=#{key}")
    int updateValue(@Param("key") String key, @Param("value") String value);
}
