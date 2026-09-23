package com.example.gov.service;

import com.example.gov.common.BizException;
import com.example.gov.entity.Admin;
import com.example.gov.entity.LoginLog;
import com.example.gov.mapper.AdminMapper;
import com.example.gov.mapper.LoginLogMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 管理员认证与权限服务。
 *
 * <p>对应 PHP 版 Admin 模型 + 后台登录控制器的核心逻辑：
 * 密码 bcrypt 校验、登录失败锁定（5 次锁定 30 分钟）、角色权限判定。</p>
 */
@Slf4j
@Service
public class AdminService {

    private final AdminMapper adminMapper;
    private final LoginLogMapper loginLogMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    @Value("${gov.security.login-max-fail:5}")
    private int maxFail;
    @Value("${gov.security.login-lock-minutes:30}")
    private int lockMinutes;

    public AdminService(AdminMapper adminMapper, LoginLogMapper loginLogMapper, ObjectMapper objectMapper) {
        this.adminMapper = adminMapper;
        this.loginLogMapper = loginLogMapper;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.objectMapper = objectMapper;
    }

    public Admin findByUsername(String username) {
        return adminMapper.findByUsername(username);
    }

    public Admin findById(Long id) {
        return adminMapper.findById(id);
    }

    /**
     * 校验管理员账号密码，成功返回管理员对象，失败抛出 BizException。
     */
    public Admin login(String username, String password, String ip, String userAgent) {
        Admin admin = adminMapper.findByUsername(username);
        if (admin == null) {
            recordLoginLog(0L, username, 0, "用户不存在", ip, userAgent);
            throw new BizException("用户名或密码错误");
        }
        if (admin.getStatus() != null && admin.getStatus() != 1) {
            throw new BizException("账号已被禁用");
        }
        if (admin.getAuthStatus() != null && admin.getAuthStatus() != 1) {
            throw new BizException("账号待授权，请联系超级管理员");
        }
        // 锁定判断
        if (admin.getLockTime() != null && admin.getLockTime().isAfter(LocalDateTime.now())) {
            long remain = java.time.Duration.between(LocalDateTime.now(), admin.getLockTime()).toMinutes() + 1;
            throw new BizException("账号已锁定，请" + remain + "分钟后重试");
        }
        // 密码校验
        if (!passwordEncoder.matches(password, admin.getPassword())) {
            int fail = (admin.getLoginFailCount() == null ? 0 : admin.getLoginFailCount()) + 1;
            String lockTime = null;
            if (fail >= maxFail) {
                lockTime = LocalDateTime.now().plusMinutes(lockMinutes)
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
            adminMapper.updateFailCount(admin.getId(), fail, lockTime);
            recordLoginLog(admin.getId(), username, 0, "密码错误", ip, userAgent);
            int remainChance = maxFail - fail;
            if (remainChance <= 0) {
                throw new BizException("密码错误次数过多，账号已锁定30分钟");
            }
            throw new BizException("密码错误，还剩" + remainChance + "次机会");
        }
        // 成功
        adminMapper.updateLoginInfo(admin.getId(), ip);
        recordLoginLog(admin.getId(), username, 1, "", ip, userAgent);
        return admin;
    }

    public void createAdmin(Admin admin, String rawPassword) {
        admin.setPassword(passwordEncoder.encode(rawPassword));
        adminMapper.insert(admin);
    }

    public void updateAdmin(Admin admin, String rawPassword) {
        adminMapper.update(admin);
        if (rawPassword != null && !rawPassword.isBlank()) {
            adminMapper.updatePassword(admin.getId(), passwordEncoder.encode(rawPassword));
        }
    }

    public void deleteAdmin(Long id) {
        adminMapper.deleteById(id);
    }

    public List<Admin> page(int page, int size) {
        return adminMapper.selectPage((page - 1) * size, size);
    }

    public long count() {
        return adminMapper.count();
    }

    /**
     * 判断管理员是否拥有某权限（超级管理员与通配符直接放行）。
     */
    public boolean hasPermission(Admin admin, String permission) {
        if (admin == null) {
            return false;
        }
        if (admin.getIsSuper() != null && admin.getIsSuper() == 1) {
            return true;
        }
        if (admin.getRoleId() == null) {
            return false;
        }
        String json = adminMapper.getRolePermissions(admin.getRoleId());
        if (json == null || json.isBlank()) {
            return false;
        }
        try {
            List<String> perms = objectMapper.readValue(json, new TypeReference<List<String>>() {
            });
            if (perms.contains("*")) {
                return true;
            }
            if (perms.contains(permission)) {
                return true;
            }
            for (String p : perms) {
                if (p.endsWith(".*") && permission.startsWith(p.substring(0, p.length() - 1))) {
                    return true;
                }
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    private void recordLoginLog(Long userId, String username, int status, String reason, String ip, String ua) {
        LoginLog log = new LoginLog();
        log.setUserId(userId);
        log.setUserType(1);
        log.setUsername(username);
        log.setLoginType(1);
        log.setLoginStatus(status);
        log.setFailReason(reason);
        log.setIpAddress(ip);
        log.setUserAgent(ua);
        try {
            loginLogMapper.insert(log);
        } catch (Exception ignored) {
        }
    }
}
