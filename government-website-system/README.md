# 政府官网系统（Java 重构版）

> 本项目由原 **PHP** 实现重构为 **Java（Spring Boot 3 + MyBatis + Thymeleaf）** 版本。
> 采用 **前后端不分离（服务端渲染）** 模式，与原版功能对等，并完成了全链路自动化测试。

---

## 一、技术栈

| 层 | 技术 | 说明 |
|---|---|---|
| 运行时 | Java 21 / Spring Boot 3.3 | 内嵌 Tomcat，可执行 jar |
| 视图 | Thymeleaf | 服务端渲染（前后端不分离），Bootstrap 5 |
| 持久层 | MyBatis 3 | 注解 + 动态 SQL，HikariCP 连接池 |
| 数据库 | MySQL 8 | 字符集 utf8mb4 |
| 缓存 | Redis (Lettuce) | 全站设置、前端文字配置缓存 |
| 安全 | BCrypt | 密码加密（spring-security-crypto），未引入完整过滤链 |
| 会话 | HttpSession | 后台管理员 / 前台用户双会话体系 |
| 构建 | Maven | `mvn package` 产出可执行 jar |
| 测试 | JUnit 5 + Spring Boot Test + MockMvc | 上下文加载 + 公开页面 + 登录鉴权 + 新增功能全链路 |

---

## 二、目录结构

```
government-website-system/
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   ├── java/com/example/gov/
    │   │   ├── GovApplication.java          # 启动类
    │   │   ├── init/DbInitializer.java      # 数据库初始化器（建库/建表/种子数据/默认管理员）
    │   │   ├── common/                       # 会话键、业务异常、分页封装
    │   │   ├── config/                       # MVC 配置、后台拦截器、全局模型注入
    │   │   ├── entity/                       # 数据库实体
    │   │   ├── mapper/                       # MyBatis 数据访问接口
    │   │   ├── service/                      # 业务服务层（含文件存储、数据备份）
    │   │   └── controller/
    │   │       ├── home/                      # 前台控制器
    │   │       └── admin/                     # 后台控制器
    │   └── resources/
    │       ├── application.yml                # 配置（数据源/Redis/端口/上传/备份）
    │       ├── sql/                          # 建表与种子 SQL
    │       ├── static/css/                  # 前台样式
    │       └── templates/                    # Thymeleaf 模板（home / admin）
    └── test/java/com/gov/gows/               # 全链路自动化测试
```

---

## 三、快速启动

### 1. 环境要求
- JDK 21+
- Maven 3.8+
- 可访问的 MySQL 与 Redis（已在 `application.yml` 中配置远程地址）

### 2. 初始化数据库（首次）
```bash
mvn compile exec:java -Dexec.mainClass=com.example.gov.init.DbInitializer
```
该脚本会自动：创建 `gov_system` 数据库 → 建表 → 导入种子数据 → 创建默认管理员。
> 注意：初始化脚本会 DROP 重建表，仅限开发环境使用，请勿在生产环境重复执行。

### 3. 运行
```bash
# 开发模式
mvn spring-boot:run

# 或打包运行
mvn -DskipTests package
java -jar target/gov-website-system.jar
```

启动后访问：
- 前台首页：http://localhost:8082/
- 后台登录：http://localhost:8082/admin/login
- 默认管理员：`admin` / `Admin@123456`

> 端口可在 `application.yml` 的 `server.port` 修改；多实例并行验证可用 `--server.port=<端口>` 覆盖。

### 4. 运行测试
```bash
mvn test
```
包含 25 个自动化用例：上下文加载、前台公开页面、后台登录鉴权、验证码、错误密码拦截，
以及本次新增功能回归（前台登录态访问后台跳转、媒体库、附件、数据备份、SMTP、登录日志）。

---

## 四、主要功能

**前台**：首页（轮播/快捷入口/最新公告政策）、政务公开（公告列表/详情）、政策法规（列表/详情，含附件下载）、
裁判文书（检索/详情，含附件下载）、公众服务（办事指南/在线咨询）、咨询投诉（提交/我的/公开问答）、
用户中心（资料/改密）、全站搜索、联系我们、用户登录/注册/退出。

**后台**：控制台仪表盘、公告/政策/裁判文书 CRUD（支持附件上传）、咨询处理与回复、前台用户管理、
管理员与角色权限管理、导航管理、**媒体库（图片预览 / 类型筛选 / 复制链接 / 大小与时间展示 / 分页 / 上传限制）**、
前端文字配置、操作日志、**登录日志查看**、**数据备份（导出 SQL / 下载 / 删除 / 恢复）**、
系统设置（**含邮件发送测试，走 application.yml 的 spring.mail.* 原生配置**）、图形验证码。

**安全**：BCrypt 密码、登录失败 5 次锁定 30 分钟、SQL 全部预编译防注入、
后台会话拦截、CSRF 关闭（服务端渲染表单同源）。

---

## 五、与原 PHP 版的对应关系与改进

| 原 PHP 概念 | Java 对应 |
|---|---|
| MVC 控制器 | Spring `@Controller` + Thymeleaf |
| PDO 表前缀查询构造器 | MyBatis Mapper |
| 数据库 session 登录态 | HttpSession |
| `cache.php` 文件缓存 | Redis（StringRedisTemplate），故障自动回退 DB |
| 基类 `fetch()` 注入 siteConfig | `HomeModelAdvice` 全局模型注入 |
| 路由分发 | Spring MVC 路由 + `AdminAuthInterceptor` |
| `Media.php` 媒体查看 | `AdminMediaController`（类型筛选/缩略图/复制链接/分页） |
| `Backup.php` 数据备份 | `BackupService` + `AdminBackupController`（JDBC 导出 SQL，无需本机 mysqldump） |

**改进点**：连接池（HikariCP）、Redis 缓存、编译期类型安全、统一异常处理、自动化测试覆盖、结构化日志。

---

## 六、本次修复与功能补齐记录（2026-09-24）

1. **修复：前台登录后访问后台报 403**
   现象：前台用户登录后（未退出），点击顶部“管理后台”直接报 HTTP 403 错误页。
   根因：`AdminAuthInterceptor` 对已存在前台登录态（`USER_ID`）但无后台登录态（`ADMIN_ID`）的会话直接 `sendError(403)`。
   修复：统一重定向到 `/admin/login` 后台登录页，并在登录页提示“当前前台账号已登录，后台请使用管理员账号登录”，
   保留前台会话，不再出现 403 错误页。
2. **媒体库“查看”能力补齐**：图片缩略图预览、类型筛选（图片/视频/音频/文档/其他）、复制链接、
   文件大小格式化、上传时间、分页、上传扩展名白名单与 10MB 大小限制。
3. **政策法规 / 裁判文书附件**：后台表单支持上传附件，前台详情页展示“附件下载”链接。
4. **数据备份**：后台一键导出全库 SQL（无需本机 mysqldump），支持下载、删除与上传 SQL 恢复（危险操作，页面二次确认）。
5. **登录日志**：后台新增登录日志查看页，支持按成功/失败筛选与分页。
6. **邮件发送（Spring Boot 原生）**：使用 `application.yml` 的 `spring.mail.*` 配置（QQ 邮箱 smtp.qq.com:587 + STARTTLS）
   与自动装配的 `JavaMailSender`，系统设置页支持向任意邮箱发送测试邮件验证配置。
7. **图片懒加载**：前台注入 IntersectionObserver 懒加载脚本（高斯模糊过渡动画），媒体库缩略图使用原生懒加载。
8. **后台表单参数名统一**：修复模板下划线字段名与控制器 camelCase 参数名不一致导致的
   发布机构、案号、角色名、导航配置等字段保存丢失问题（如 `publish_org`/`case_no`/`role_name`/`nav_name` 等）。

---

## 七、配置说明

所有配置集中在 `src/main/resources/application.yml`：
- `spring.datasource.*`：MySQL 连接（IP/端口/账号/密码）
- `spring.data.redis.*`：Redis 连接
- `server.port`：服务端口（默认 8082）
- `gov.upload-path` / `gov.upload-allowed-ext`：上传目录与允许扩展名
- `gov.backup-path`：数据备份文件目录
- `spring.mail.*`：SMTP 邮件配置（host/port/username/password/starttls），用于后台测试邮件发送
- `gov.security.*`：登录失败锁定策略
- `gov.cache.type`：缓存开关（redis / 本地回退）

生产部署请修改为生产环境的数据库与 Redis 地址。
