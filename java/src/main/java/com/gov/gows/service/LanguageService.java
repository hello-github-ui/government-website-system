package com.gov.gows.service;

import com.gov.gows.entity.Language;
import com.gov.gows.mapper.LanguageMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 前端文字配置服务（带 Redis 缓存）。
 */
@Service
public class LanguageService {

    private final LanguageMapper languageMapper;
    private final StringRedisTemplate redis;

    public LanguageService(LanguageMapper languageMapper, StringRedisTemplate redis) {
        this.languageMapper = languageMapper;
        this.redis = redis;
    }

    /** 获取某模块全部文字配置（key -> value）。 */
    public Map<String, String> getModuleLang(String module) {
        String cacheKey = "gov:lang_module_" + module;
        try {
            String cached = redis.opsForValue().get(cacheKey);
            if (cached != null) {
                return parse(cached);
            }
        } catch (Exception ignored) {
        }
        List<Language> list = languageMapper.selectByModule(module);
        Map<String, String> map = new HashMap<>();
        for (Language l : list) {
            map.put(l.getLangKey(), l.getLangValue());
        }
        try {
            redis.opsForValue().set(cacheKey, toJson(map), 1, TimeUnit.HOURS);
        } catch (Exception ignored) {
        }
        return map;
    }

    /** 获取通用模块文字。 */
    public Map<String, String> commonLang() {
        return getModuleLang("common");
    }

    public List<Language> all() {
        return languageMapper.selectByGroup("common", "common");
    }

    public void updateValue(String key, String value) {
        languageMapper.updateValueByKey(key, value);
        clearCache();
    }

    public void clearCache() {
        try {
            redis.delete("gov:lang_module_common");
            redis.delete("gov:lang_module_judicial");
        } catch (Exception ignored) {
        }
    }

    private Map<String, String> parse(String json) {
        Map<String, String> map = new HashMap<>();
        // 简易解析：使用 Jackson
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper()
                    .readValue(json, new com.fasterxml.jackson.core.type.TypeReference<Map<String, String>>() {});
        } catch (Exception e) {
            return map;
        }
    }

    private String toJson(Map<String, String> map) {
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(map);
        } catch (Exception e) {
            return "{}";
        }
    }
}
