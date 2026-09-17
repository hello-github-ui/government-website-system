package com.gov.gows.service;

import com.gov.gows.common.BizException;
import com.gov.gows.entity.User;
import com.gov.gows.mapper.UserMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 前台用户认证服务（登录/注册/改密/锁定）。
 */
@Service
public class UserService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    public User findById(Long id) {
        return userMapper.findById(id);
    }

    /** 前台登录校验，成功返回用户，失败抛异常。 */
    public User login(String username, String password, String ip) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new BizException("用户名或密码错误");
        }
        if (user.getStatus() != null && user.getStatus() != 1) {
            throw new BizException("账号已被禁用");
        }
        if (user.getLockTime() != null && user.getLockTime().isAfter(LocalDateTime.now())) {
            long remain = java.time.Duration.between(LocalDateTime.now(), user.getLockTime()).toMinutes() + 1;
            throw new BizException("账号已锁定，请" + remain + "分钟后重试");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            int fail = (user.getLoginFailCount() == null ? 0 : user.getLoginFailCount()) + 1;
            String lockTime = null;
            if (fail >= 5) {
                lockTime = LocalDateTime.now().plusMinutes(30)
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
            userMapper.updateFailCount(user.getId(), fail, lockTime);
            int remainChance = 5 - fail;
            if (remainChance <= 0) {
                throw new BizException("密码错误次数过多，账号已锁定30分钟");
            }
            throw new BizException("密码错误，还剩" + remainChance + "次机会");
        }
        userMapper.updateLoginInfo(user.getId(), ip);
        return user;
    }

    /** 前台注册。 */
    public void register(String username, String password, String confirmPassword,
                         String realName, String phone, String email) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new BizException("用户名和密码不能为空");
        }
        if (username.length() < 3 || username.length() > 20) {
            throw new BizException("用户名长度为3-20个字符");
        }
        if (!password.equals(confirmPassword)) {
            throw new BizException("两次输入的密码不一致");
        }
        if (password.length() < 6) {
            throw new BizException("密码长度至少6位");
        }
        if (email != null && !email.isBlank() && !email.matches("^[\\w.+-]+@[\\w-]+\\.[\\w.-]+$")) {
            throw new BizException("邮箱格式不正确");
        }
        if (userMapper.findByUsername(username) != null) {
            throw new BizException("用户名已存在");
        }
        if (email != null && !email.isBlank() && userMapper.findByEmail(email) != null) {
            throw new BizException("邮箱已被注册");
        }
        User u = new User();
        u.setUsername(username);
        u.setPassword(passwordEncoder.encode(password));
        u.setRealName(realName);
        u.setPhone(phone);
        u.setEmail(email);
        u.setStatus(1);
        userMapper.insert(u);
    }

    /** 修改密码。 */
    public void changePassword(Long userId, String oldPwd, String newPwd, String confirmPwd) {
        if (oldPwd == null || newPwd == null || confirmPwd == null
                || oldPwd.isBlank() || newPwd.isBlank()) {
            throw new BizException("请填写所有密码字段");
        }
        if (!newPwd.equals(confirmPwd)) {
            throw new BizException("两次输入的新密码不一致");
        }
        if (newPwd.length() < 6) {
            throw new BizException("新密码长度至少6位");
        }
        User user = userMapper.findById(userId);
        if (user == null || !passwordEncoder.matches(oldPwd, user.getPassword())) {
            throw new BizException("原密码错误");
        }
        userMapper.updatePassword(userId, passwordEncoder.encode(newPwd));
    }

    public org.springframework.data.domain.Page<User> page(int page, int size) {
        var list = userMapper.selectPage((page - 1) * size, size);
        long total = userMapper.count();
        return new org.springframework.data.domain.PageImpl<>(list,
                org.springframework.data.domain.PageRequest.of(page - 1, size), total);
    }

    /** 后台更新用户资料；若传入新密码则同时更新密码。 */
    public void update(User user, String rawPassword) {
        userMapper.update(user);
        if (rawPassword != null && !rawPassword.isBlank()) {
            userMapper.updatePassword(user.getId(), passwordEncoder.encode(rawPassword));
        }
    }

    /** 后台删除用户。 */
    public void delete(Long id) {
        userMapper.deleteById(id);
    }
}
