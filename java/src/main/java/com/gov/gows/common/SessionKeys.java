package com.gov.gows.common;

/**
 * 会话(Session)键常量，统一前台用户与后台管理员的会话存储。
 */
public final class SessionKeys {

    private SessionKeys() {}

    /** 后台管理员ID */
    public static final String ADMIN_ID = "admin_id";
    /** 后台管理员信息(Map: id/username/name/isSuper) */
    public static final String ADMIN = "admin";
    /** 前台用户ID */
    public static final String USER_ID = "user_id";
    /** 前台用户名 */
    public static final String USER_NAME = "user_name";
    /** 闪存消息 */
    public static final String FLASH_MESSAGE = "flash_message";
}
