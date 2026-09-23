-- ===================================================================
-- 政府官网系统数据库结构（Java 重构版初始化脚本）
-- 字符集: utf8mb4  该脚本可重复执行（DROP TABLE IF EXISTS）
-- ===================================================================
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 管理员表
-- ----------------------------
DROP TABLE IF EXISTS `gov_admin`;
CREATE TABLE `gov_admin` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
    `username` varchar(50) NOT NULL COMMENT '登录账号',
    `password` varchar(255) NOT NULL COMMENT '密码(bcrypt加密)',
    `name` varchar(50) NOT NULL COMMENT '真实姓名',
    `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
    `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
    `role_id` int(11) unsigned DEFAULT '0' COMMENT '角色ID',
    `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
    `is_super` tinyint(1) unsigned DEFAULT '0' COMMENT '是否超级管理员 0否 1是',
    `is_admin` tinyint(1) unsigned DEFAULT '1' COMMENT '是否管理员',
    `auth_status` tinyint(1) unsigned DEFAULT '0' COMMENT '授权状态 0待授权 1已授权 2已拒绝',
    `auth_time` datetime DEFAULT NULL COMMENT '授权时间',
    `auth_expire_time` datetime DEFAULT NULL COMMENT '授权过期时间',
    `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
    `last_login_ip` varchar(50) DEFAULT NULL COMMENT '最后登录IP',
    `login_fail_count` int(11) unsigned DEFAULT '0' COMMENT '登录失败次数',
    `lock_time` datetime DEFAULT NULL COMMENT '锁定时间',
    `status` tinyint(1) unsigned DEFAULT '1' COMMENT '状态 0禁用 1启用',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `username` (`username`),
    KEY `role_id` (`role_id`),
    KEY `status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员表';

-- ----------------------------
-- 前台用户表
-- ----------------------------
DROP TABLE IF EXISTS `gov_user`;
CREATE TABLE `gov_user` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
    `username` varchar(50) NOT NULL COMMENT '登录账号',
    `password` varchar(255) NOT NULL COMMENT '密码(bcrypt加密)',
    `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
    `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
    `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
    `id_card` varchar(50) DEFAULT NULL COMMENT '身份证号(脱敏存储)',
    `user_intro` text COMMENT '个人简介',
    `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
    `is_admin` tinyint(1) unsigned DEFAULT '0' COMMENT '是否管理员',
    `auth_status` tinyint(1) unsigned DEFAULT '0' COMMENT '授权状态',
    `auth_time` datetime DEFAULT NULL,
    `auth_expire_time` datetime DEFAULT NULL,
    `last_login_time` datetime DEFAULT NULL,
    `last_login_ip` varchar(50) DEFAULT NULL,
    `login_fail_count` int(11) unsigned DEFAULT '0',
    `lock_time` datetime DEFAULT NULL,
    `status` tinyint(1) unsigned DEFAULT '1' COMMENT '0禁用 1启用',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `username` (`username`),
    KEY `status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='前台用户表';

-- ----------------------------
-- 管理员角色表
-- ----------------------------
DROP TABLE IF EXISTS `gov_admin_role`;
CREATE TABLE `gov_admin_role` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
    `role_name` varchar(50) NOT NULL COMMENT '角色名称',
    `role_desc` varchar(255) DEFAULT NULL COMMENT '角色描述',
    `permissions` text COMMENT '权限列表(JSON)',
    `sort` int(11) unsigned DEFAULT '0',
    `status` tinyint(1) unsigned DEFAULT '1',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员角色表';

-- ----------------------------
-- 网站设置表
-- ----------------------------
DROP TABLE IF EXISTS `gov_settings`;
CREATE TABLE `gov_settings` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
    `setting_key` varchar(100) NOT NULL,
    `setting_value` text,
    `setting_group` varchar(50) DEFAULT 'general',
    `setting_desc` varchar(255) DEFAULT NULL,
    `is_system` tinyint(1) unsigned DEFAULT '0',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `setting_key` (`setting_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='网站设置表';

-- ----------------------------
-- 前端文字配置表
-- ----------------------------
DROP TABLE IF EXISTS `gov_language`;
CREATE TABLE `gov_language` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
    `lang_key` varchar(200) NOT NULL,
    `lang_value` text,
    `lang_group` varchar(50) DEFAULT 'common',
    `module` varchar(50) DEFAULT 'common',
    `is_default` tinyint(1) unsigned DEFAULT '0',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `lang_key` (`lang_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='前端文字配置表';

-- ----------------------------
-- 导航菜单表
-- ----------------------------
DROP TABLE IF EXISTS `gov_nav`;
CREATE TABLE `gov_nav` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
    `parent_id` int(11) unsigned DEFAULT '0',
    `nav_name` varchar(100) NOT NULL,
    `nav_url` varchar(255) DEFAULT NULL,
    `nav_type` tinyint(1) unsigned DEFAULT '1',
    `target` varchar(20) DEFAULT '_self',
    `icon` varchar(100) DEFAULT NULL,
    `sort` int(11) unsigned DEFAULT '0',
    `is_show` tinyint(1) unsigned DEFAULT '1',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `parent_id` (`parent_id`),
    KEY `sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='导航菜单表';

-- ----------------------------
-- 媒体文件表
-- ----------------------------
DROP TABLE IF EXISTS `gov_media`;
CREATE TABLE `gov_media` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
    `user_id` int(11) unsigned DEFAULT '0',
    `user_type` tinyint(1) unsigned DEFAULT '1',
    `file_name` varchar(255) NOT NULL,
    `file_hash` varchar(64) NOT NULL,
    `file_path` varchar(500) NOT NULL,
    `file_url` varchar(500) NOT NULL,
    `file_type` varchar(50) NOT NULL,
    `file_mime` varchar(100) DEFAULT NULL,
    `file_size` int(11) unsigned DEFAULT '0',
    `file_ext` varchar(20) DEFAULT NULL,
    `image_width` int(11) unsigned DEFAULT '0',
    `image_height` int(11) unsigned DEFAULT '0',
    `is_used` tinyint(1) unsigned DEFAULT '0',
    `used_count` int(11) unsigned DEFAULT '0',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `file_hash` (`file_hash`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='媒体文件表';

-- ----------------------------
-- 公告表
-- ----------------------------
DROP TABLE IF EXISTS `gov_notice`;
CREATE TABLE `gov_notice` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
    `title` varchar(255) NOT NULL,
    `content` longtext,
    `summary` text,
    `cover` varchar(255) DEFAULT NULL,
    `author` varchar(50) DEFAULT NULL,
    `source` varchar(100) DEFAULT NULL,
    `views` int(11) unsigned DEFAULT '0',
    `sort` int(11) unsigned DEFAULT '0',
    `is_top` tinyint(1) unsigned DEFAULT '0',
    `is_important` tinyint(1) unsigned DEFAULT '0',
    `status` tinyint(1) unsigned DEFAULT '1' COMMENT '0草稿 1已发布 2已下架',
    `publish_time` datetime DEFAULT NULL,
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `status` (`status`),
    KEY `is_top` (`is_top`),
    KEY `publish_time` (`publish_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公告表';

-- ----------------------------
-- 政策法规表
-- ----------------------------
DROP TABLE IF EXISTS `gov_policy`;
CREATE TABLE `gov_policy` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
    `title` varchar(255) NOT NULL,
    `content` longtext,
    `summary` text,
    `doc_no` varchar(100) DEFAULT NULL,
    `publish_org` varchar(100) DEFAULT NULL,
    `publish_date` date DEFAULT NULL,
    `effective_date` date DEFAULT NULL,
    `category_id` int(11) unsigned DEFAULT '0',
    `attachment` varchar(255) DEFAULT NULL,
    `views` int(11) unsigned DEFAULT '0',
    `status` tinyint(1) unsigned DEFAULT '1',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `category_id` (`category_id`),
    KEY `status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='政策法规表';

-- ----------------------------
-- 裁判文书表
-- ----------------------------
DROP TABLE IF EXISTS `gov_judicial`;
CREATE TABLE `gov_judicial` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
    `case_no` varchar(100) NOT NULL,
    `case_name` varchar(255) NOT NULL,
    `court` varchar(100) NOT NULL,
    `case_type` varchar(50) DEFAULT NULL,
    `case_cause` varchar(200) DEFAULT NULL,
    `judge_date` date DEFAULT NULL,
    `publish_date` date DEFAULT NULL,
    `content` longtext,
    `content_desensitized` longtext,
    `parties` text,
    `judge` varchar(50) DEFAULT NULL,
    `clerk` varchar(50) DEFAULT NULL,
    `attachment` varchar(255) DEFAULT NULL,
    `views` int(11) unsigned DEFAULT '0',
    `download_count` int(11) unsigned DEFAULT '0',
    `check_status` tinyint(1) unsigned DEFAULT '0' COMMENT '0待审核 1已通过 2已驳回',
    `check_remark` text,
    `is_public` tinyint(1) unsigned DEFAULT '0',
    `status` tinyint(1) unsigned DEFAULT '1',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `case_no` (`case_no`),
    KEY `court` (`court`),
    KEY `case_type` (`case_type`),
    KEY `judge_date` (`judge_date`),
    KEY `check_status` (`check_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='裁判文书表';

-- ----------------------------
-- 咨询投诉表
-- ----------------------------
DROP TABLE IF EXISTS `gov_consult`;
CREATE TABLE `gov_consult` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
    `user_id` int(11) unsigned DEFAULT '0',
    `type` tinyint(1) unsigned DEFAULT '1' COMMENT '1咨询 2投诉 3建议',
    `title` varchar(255) NOT NULL,
    `content` text,
    `contact_name` varchar(50) DEFAULT NULL,
    `contact_phone` varchar(20) DEFAULT NULL,
    `contact_email` varchar(100) DEFAULT NULL,
    `attachment` varchar(255) DEFAULT NULL,
    `reply_content` text,
    `reply_time` datetime DEFAULT NULL,
    `reply_user_id` int(11) unsigned DEFAULT '0',
    `status` tinyint(1) unsigned DEFAULT '0' COMMENT '0待处理 1处理中 2已回复 3已关闭',
    `is_public` tinyint(1) unsigned DEFAULT '0',
    `ip_address` varchar(50) DEFAULT NULL,
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `type` (`type`),
    KEY `status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='咨询投诉表';

-- ----------------------------
-- 操作日志表
-- ----------------------------
DROP TABLE IF EXISTS `gov_operation_log`;
CREATE TABLE `gov_operation_log` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
    `user_id` int(11) unsigned DEFAULT '0',
    `user_type` tinyint(1) unsigned DEFAULT '1' COMMENT '1管理员 2前台用户',
    `username` varchar(50) DEFAULT NULL,
    `module` varchar(50) DEFAULT NULL COMMENT '操作模块',
    `action` varchar(50) DEFAULT NULL COMMENT '操作动作',
    `content` text,
    `ip_address` varchar(50) DEFAULT NULL,
    `user_agent` varchar(500) DEFAULT NULL,
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `user_id` (`user_id`),
    KEY `module` (`module`),
    KEY `create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- ----------------------------
-- 登录日志表
-- ----------------------------
DROP TABLE IF EXISTS `gov_login_log`;
CREATE TABLE `gov_login_log` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
    `user_id` int(11) unsigned DEFAULT '0',
    `user_type` tinyint(1) unsigned DEFAULT '1',
    `username` varchar(50) DEFAULT NULL,
    `login_type` tinyint(1) unsigned DEFAULT '1',
    `login_status` tinyint(1) unsigned DEFAULT '1' COMMENT '1成功 0失败',
    `fail_reason` varchar(255) DEFAULT NULL,
    `ip_address` varchar(50) DEFAULT NULL,
    `user_agent` varchar(500) DEFAULT NULL,
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `login_status` (`login_status`),
    KEY `create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='登录日志表';

SET FOREIGN_KEY_CHECKS = 1;
