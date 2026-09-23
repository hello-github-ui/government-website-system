package com.example.gov.init;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * 数据库初始化器（一次性、幂等）。
 *
 * <p>职责：
 * <ol>
 *   <li>连接 MySQL 服务器并创建 {@code gov_system} 数据库（如不存在）；</li>
 *   <li>执行 {@code schema.sql} 创建全部表结构；</li>
 *   <li>执行 {@code data.sql} 写入种子数据（角色/设置/文字/示例内容）；</li>
 *   <li>创建默认超级管理员账号（admin / Admin@123456，bcrypt 加密）。</li>
 * </ol>
 *
 * <p>该类使用原生 JDBC，不依赖 Spring 容器，可独立运行。
 * 重复执行会 DROP 并重建表（开发环境专用）。
 */
public class DbInitializer {

    private static final String HOST = "106.75.179.59";
    private static final String PORT = "3306";
    private static final String USER = "root";
    private static final String PASSWORD = "6228332zzz@@@";
    private static final String DB = "gov_system";

    public static void main(String[] args) throws Exception {
        // 1. 连接服务器（不指定数据库），创建数据库
        String serverUrl = String.format(
            "jdbc:mysql://%s:%s/?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true",
            HOST, PORT);
        try (Connection conn = DriverManager.getConnection(serverUrl, USER, PASSWORD);
             Statement st = conn.createStatement()) {
            st.execute("CREATE DATABASE IF NOT EXISTS `" + DB + "` "
                + "DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
            System.out.println("[OK] 数据库 " + DB + " 已就绪");
        }

        // 2. 连接目标库，执行建表脚本与种子数据
        String dbUrl = String.format(
            "jdbc:mysql://%s:%s/%s?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&allowMultiQueries=true",
            HOST, PORT, DB);
        try (Connection conn = DriverManager.getConnection(dbUrl, USER, PASSWORD)) {
            executeSqlResource(conn, "sql/schema.sql");
            System.out.println("[OK] 表结构初始化完成");

            executeSqlResource(conn, "sql/data.sql");
            System.out.println("[OK] 种子数据导入完成");

            ensureDefaultAdmin(conn);
            System.out.println("[OK] 默认管理员已就绪 (admin / Admin@123456)");
        }
        System.out.println("===== 数据库初始化全部完成 =====");
    }

    /**
     * 执行 classpath 下的 SQL 脚本（按分号拆分语句逐条执行）。
     */
    private static void executeSqlResource(Connection conn, String resource) throws Exception {
        String sql = readResource(resource);
        // 去掉注释行后按分号拆分
        List<String> statements = Arrays.stream(sql.split(";"))
            .map(s -> s.replaceAll("--[^\\n]*", "").trim())
            .filter(s -> !s.isEmpty())
            .toList();
        try (Statement st = conn.createStatement()) {
            for (String s : statements) {
                st.execute(s);
            }
        }
    }

    /**
     * 创建默认超级管理员（幂等：已存在则跳过）。
     */
    private static void ensureDefaultAdmin(Connection conn) throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("Admin@123456");
        // ON DUPLICATE KEY UPDATE 不适用（首次建表无数据），先查再插
        try (Statement st = conn.createStatement()) {
            var rs = st.executeQuery("SELECT COUNT(*) FROM `gov_admin` WHERE username='admin'");
            rs.next();
            if (rs.getInt(1) > 0) {
                System.out.println("[..] 管理员 admin 已存在，跳过创建");
                return;
            }
        }
        String insert = "INSERT INTO `gov_admin` "
            + "(username, password, name, role_id, is_super, is_admin, auth_status, status) "
            + "VALUES ('admin', '" + hash + "', '超级管理员', 1, 1, 1, 1, 1)";
        try (Statement st = conn.createStatement()) {
            st.execute(insert);
        }
    }

    private static String readResource(String resource) throws Exception {
        try (InputStream in = DbInitializer.class.getClassLoader().getResourceAsStream(resource)) {
            if (in == null) {
                throw new IllegalStateException("找不到资源: " + resource);
            }
            return new Scanner(in, StandardCharsets.UTF_8).useDelimiter("\\A").next();
        }
    }
}
