# 部署运维指南（Java 重构版）

## 一、环境要求
- JDK 21+（必须，Spring Boot 3 最低要求 Java 17）
- Maven 3.8+（仅构建时需要）
- MySQL 8+、Redis 6+（已配置远程：106.75.179.59）

## 二、初始化数据库
首次部署执行一次：
```bash
mvn -q compile exec:java -Dexec.mainClass=com.example.gov.init.DbInitializer
```
> 该脚本幂等：DROP 后重建表并写入种子数据，仅用于初始化，不要在生产重复执行。

## 三、构建与启动
```bash
mvn -DskipTests package
# 产物：target/gov-website-system.jar
java -jar target/gov-website-system.jar --server.port=8082
```
后台常驻（Linux systemd 示例）：
```ini
[Unit]
Description=Gov Website System
After=network.target

[Service]
WorkingDirectory=/opt/gov
ExecStart=/usr/bin/java -jar /opt/gov/gov-website-system.jar
Restart=always
User=app

[Install]
WantedBy=multi-user.target
```

## 四、运维检查
| 检查项 | 命令 / 地址 |
|---|---|
| 服务存活 | `curl http://localhost:8082/` |
| 后台登录 | http://服务器IP:8082/admin/login （admin / Admin@123456） |
| 健康日志 | 控制台输出 / logs |
| 数据库连接池 | HikariCP，见 application.yml `spring.datasource.hikari` |
| Redis 缓存 | 设置页"清除缓存"按钮；key 前缀 `gov:` |
| 数据备份 | 后台「数据备份」一键导出 SQL，文件在运行目录 `backup/` |

## 五、默认账号
- 超级管理员：`admin` / `Admin@123456`
- **首次登录后请立即在后台修改密码。**

## 六、安全建议
1. 修改 `application.yml` 中的数据库与 Redis 密码；
2. 生产环境将 `server.port` 前置 Nginx 并配置 HTTPS；
3. 限制 MySQL/Redis 安全组，仅允许应用服务器访问；
4. `uploads/`、`backup/` 目录单独挂载，定期备份。

## 七、常见问题
- **前台 500 / 后台白屏**：多为模板缓存或数据库未初始化，确认已执行 DbInitializer。
- **登录提示锁定**：连续失败 5 次锁定 30 分钟，等待或在数据库 `gov_admin.lock_time` 置空。
- **上传失败**：确认运行目录下 `uploads/` 可写。
