package com.example.gov.service;

import com.example.gov.common.PageResult;
import com.example.gov.entity.OperationLog;
import com.example.gov.mapper.OperationLogMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 操作日志服务。
 */
@Slf4j
@Service
public class LogService {

    private final OperationLogMapper operationLogMapper;

    public LogService(OperationLogMapper operationLogMapper) {
        this.operationLogMapper = operationLogMapper;
    }

    /**
     * 记录一条操作日志。
     */
    public void log(Long userId, String username, String module, String action,
                    String content, HttpServletRequest request) {
        OperationLog log = new OperationLog();
        log.setUserId(userId == null ? 0 : userId);
        log.setUserType(1);
        log.setUsername(username == null ? "system" : username);
        log.setModule(module);
        log.setAction(action);
        log.setContent(content);
        if (request != null) {
            log.setIpAddress(getClientIp(request));
            log.setUserAgent(request.getHeader("User-Agent"));
        }
        try {
            operationLogMapper.insert(log);
        } catch (Exception e) {
            org.slf4j.LoggerFactory.getLogger(LogService.class).warn("[{}] 操作日志写入失败 module={}, action={}, 原因={}", Thread.currentThread().getName(), module, action, e.getMessage());
        }
    }

    public PageResult<OperationLog> page(int page, int size) {
        int offset = (page - 1) * size;
        List<OperationLog> list = operationLogMapper.selectPage(offset, size);
        return new PageResult<>(list, operationLogMapper.count(), page, size);
    }

    public OperationLog findById(Long id) {
        return operationLogMapper.findById(id);
    }

    public void delete(Long id) {
        operationLogMapper.deleteById(id);
    }

    public void clearOlderThan(int days) {
        operationLogMapper.clearOlderThan(days);
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isBlank()) {
            ip = request.getRemoteAddr();
        } else {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
