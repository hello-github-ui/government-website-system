package com.gov.gows.service;

import com.gov.gows.common.PageResult;
import com.gov.gows.entity.OperationLog;
import com.gov.gows.mapper.OperationLogMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 操作日志服务。
 */
@Service
public class LogService {

    private final OperationLogMapper operationLogMapper;

    public LogService(OperationLogMapper operationLogMapper) {
        this.operationLogMapper = operationLogMapper;
    }

    /** 记录一条操作日志。 */
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
        } catch (Exception ignored) {
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
