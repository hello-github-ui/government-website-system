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
| 测试 | JUnit 5 + Spring Boot Test + MockMvc | 上下文加载 + 公开页面 + 登录鉴权全链路 |

---

## 二、目录结构

```
java/
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   ├── java/com/gov/gows/
    │   │   ├── GovApplication.java          # 启动类
    │   │   ├── init/DbInitializer.java      # 数据库初始化器（建库/建表/种子数据/默认管理员）
    │   │   ├── common/                       # 会话键、业务异常、分页封装
    │   │   ├── config/                       # MVC 配置、后台拦截器、全局模型注入
    │   │   ├── entity/                       # 数据库实体
    │   │   ├── mapper/                       # MyBatis 数据访问接口
    │   │   ├── service/                      # 业务服务层
    │   │   └── controller/
    │   │       ├── home/                      # 前台控制器
    │   │       └── admin/                     # 后台控制器
    │   └── resources/
    │       ├── application.yml                # 配置（数据源/Redis/端口）
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
mvn compile exec:java -Dexec.mainClass=com.gov.gows.init.DbInitializer
```
该脚本会自动：创建 `gov_system` 数据库 → 建表 → 导入种子数据 → 创建默认管理员。

### 3. 运行
```bash
# 开发模式
mvn spring-boot:run

# 或打包运行
mvn -DskipTests package
java -jar target/gov-website-system.jar
```

启动后访问：
- 前台首页：http://localhost:8080/
- 后台登录：http://localhost:8080/admin/login
- 默认管理员：`admin` / `Admin@123456`

### 4. 运行测试
```bash
mvn test
```
包含 15 个自动化用例：上下文加载、前台公开页面、后台登录鉴权、验证码、错误密码拦截。

---

## 四、主要功能

**前台**：首页（轮播/快捷入口/最新公告政策）、政务公开（公告列表/详情）、政策法规（列表/详情）、
裁判文书（检索/详情）、公众服务（办事指南/在线咨询）、咨询投诉（提交/我的/公开问答）、
用户中心（资料/改密）、全站搜索、联系我们、用户登录/注册/退出。

**后台**：控制台仪表盘、公告/政策/裁判文书 CRUD、咨询处理与回复、前台用户管理、
管理员与角色权限管理、导航管理、媒体库上传、前端文字配置、操作日志、系统设置、图形验证码。

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

**改进点**：连接池（HikariCP）、Redis 缓存、编译期类型安全、统一异常处理、自动化测试覆盖、结构化日志。

---

## 六、配置说明

所有配置集中在 `src/main/resources/application.yml`：
- `spring.datasource.*`：MySQL 连接（IP/端口/账号/密码）
- `spring.data.redis.*`：Redis 连接
- `server.port`：服务端口（默认 8080）
- `gov.*`：上传目录、登录失败策略、缓存开关

生产部署请修改为生产环境的数据库与 Redis 地址。
