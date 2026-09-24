package com.example.gov.service;

import com.example.gov.entity.Setting;
import com.example.gov.mapper.SettingMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 网站设置服务。
 *
 * <p>所有设置以 key-value 形式存储在 gov_settings 表，并整体缓存到 Redis（key=settings_all），
 * 读取走缓存、写入时刷新缓存，对应 PHP 版 Settings::getAllSettings() 逻辑。</p>
 */
@Slf4j
@Service
public class SettingService {

    private static final String CACHE_KEY = "gov:settings_all";

    private final SettingMapper settingMapper;
    private final StringRedisTemplate redis;
    private final ObjectMapper objectMapper;

    /**
     * 是否启用 Redis 缓存，由配置 gov.cache.type 决定
     */
    @Value("${gov.cache.type:redis}")
    private String cacheType;

    public SettingService(SettingMapper settingMapper, StringRedisTemplate redis, ObjectMapper objectMapper) {
        this.settingMapper = settingMapper;
        this.redis = redis;
        this.objectMapper = objectMapper;
    }

    /**
     * 获取全部设置（key -> value），优先读缓存。
     */
    public Map<String, String> getAll() {
        if ("redis".equalsIgnoreCase(cacheType)) {
            try {
                String cached = redis.opsForValue().get(CACHE_KEY);
                if (cached != null) {
                    return objectMapper.readValue(cached, new TypeReference<Map<String, String>>() {
                    });
                }
            } catch (Exception ignored) {
                // Redis 不可用时回源数据库
            }
        }
        List<Setting> rows = settingMapper.selectAll();
        Map<String, String> result = new HashMap<>();
        for (Setting s : rows) {
            result.put(s.getSettingKey(), s.getSettingValue());
        }
        if ("redis".equalsIgnoreCase(cacheType)) {
            try {
                redis.opsForValue().set(CACHE_KEY, objectMapper.writeValueAsString(result), 1, TimeUnit.HOURS);
            } catch (Exception ignored) {
            }
        }
        return result;
    }

    /**
     * 读取单个设置项。
     */
    public String get(String key) {
        return getAll().get(key);
    }

    public String get(String key, String defaultValue) {
        String v = get(key);
        return v == null || v.isEmpty() ? defaultValue : v;
    }

    /**
     * 保存单个设置项（存在则更新，不存在则插入）。
     */
    public void set(String key, String value) {
        String thread = Thread.currentThread().getName();
        log.info("[{}] 保存系统设置 key={}, value长度={}", thread, key, value == null ? 0 : value.length());
        Setting exist = settingMapper.findByKey(key);
        if (exist != null) {
            settingMapper.updateValue(key, value);
        } else {
            Setting s = new Setting();
            s.setSettingKey(key);
            s.setSettingValue(value);
            s.setSettingGroup("general");
            settingMapper.insert(s);
        }
        clearCache();
        log.info("[{}] 系统设置已保存 key={}", thread, key);
    }

    /**
     * 批量保存。
     */
    public void saveBatch(Map<String, String> data) {
        data.forEach(this::set);
    }

    /**
     * 清除设置缓存。
     */
    public void clearCache() {
        if ("redis".equalsIgnoreCase(cacheType)) {
            try {
                redis.delete(CACHE_KEY);
            } catch (Exception ignored) {
            }
        }
    }
}
