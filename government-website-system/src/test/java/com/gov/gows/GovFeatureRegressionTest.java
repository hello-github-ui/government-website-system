package com.example.gov;

import com.example.gov.entity.Policy;
import com.example.gov.service.BackupService;
import com.example.gov.service.PolicyService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 修复项与新增功能回归测试：
 * 1. 前台用户已登录（未退出）时访问后台，应跳转登录页而非 403；
 * 2. 后台登录后媒体库 / 数据备份 / 登录日志 / 系统设置页面可正常渲染；
 * 3. 政策法规支持附件上传，前台详情页展示附件下载；
 * 4. 数据备份可创建并清理。
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GovFeatureRegressionTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PolicyService policyService;

    @Autowired
    private BackupService backupService;

    /**
     * Mock 邮件发送器：测试环境不真实联网发信，仅验证调用链与页面行为。
     */
    @MockBean
    private JavaMailSender mailSender;

    /**
     * 通过真实登录获取后台管理员会话。
     */
    private MockHttpSession adminSession() throws Exception {
        MvcResult r = mockMvc.perform(post("/admin/login/doLogin")
                        .param("username", "admin")
                        .param("password", "Admin@123456"))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        return (MockHttpSession) r.getRequest().getSession(false);
    }

    /** 模拟前台用户已登录的会话（前台登录链路已在 HomePageFlowTest 覆盖）。 */
    private MockHttpSession frontUserSession() {
        MockHttpSession s = new MockHttpSession();
        s.setAttribute("user_id", 1L);
        s.setAttribute("user_name", "frontuser");
        return s;
    }

    // ---------------- 问题①修复：前台登录态访问后台 ----------------

    @Test
    @Order(1)
    void frontUserLoggedIn_visitAdmin_redirectsToLoginInsteadOf403() throws Exception {
        mockMvc.perform(get("/admin").session(frontUserSession()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/login"));
    }

    @Test
    @Order(2)
    void frontUserLoggedIn_adminLoginPage_rendersWithNotice() throws Exception {
        mockMvc.perform(get("/admin/login").session(frontUserSession()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("frontNotice"));
    }

    @Test
    @Order(3)
    void frontUserLoggedIn_visitAdminLoginDoLogin_whitelisted() throws Exception {
        // 白名单路径不应被拦截器拦截
        mockMvc.perform(get("/admin/captcha").session(frontUserSession()))
                .andExpect(status().isOk());
    }

    // ---------------- 后台新增管理页面可访问 ----------------

    @Test
    @Order(4)
    void adminLoggedIn_dashboardAndNewPages_return200() throws Exception {
        MockHttpSession session = adminSession();
        mockMvc.perform(get("/admin").session(session)).andExpect(status().isOk());
        mockMvc.perform(get("/admin/media").session(session)).andExpect(status().isOk())
                .andExpect(model().attributeExists("list"));
        mockMvc.perform(get("/admin/backup").session(session)).andExpect(status().isOk())
                .andExpect(model().attributeExists("files"));
        mockMvc.perform(get("/admin/log/login").session(session)).andExpect(status().isOk())
                .andExpect(model().attributeExists("list"));
        mockMvc.perform(get("/admin/settings").session(session)).andExpect(status().isOk())
                .andExpect(model().attributeExists("config"));
    }

    @Test
    @Order(5)
    void mediaList_typeFilter_works() throws Exception {
        MockHttpSession session = adminSession();
        mockMvc.perform(get("/admin/media").param("type", "image").session(session))
                .andExpect(status().isOk())
                .andExpect(model().attribute("type", "image"));
        mockMvc.perform(get("/admin/media").param("type", "document").session(session))
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    void mediaUpload_uploadsFile_thenDelete() throws Exception {
        MockHttpSession session = adminSession();
        MockMultipartFile file = new MockMultipartFile("file", "reg-test.txt", "text/plain",
            "regression test".getBytes(StandardCharsets.UTF_8));
        // 上传前统计 uploads 目录中 txt 文件数量
        Path uploadDir = Paths.get("./uploads/");
        long before = countTxt(uploadDir);
        mockMvc.perform(multipart("/admin/media/upload").file(file).session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/media"));
        long after = countTxt(uploadDir);
        // 上传后 uploads 目录应新增一个 txt 文件（文件名被重命名为 日期_UUID.txt）
        org.junit.jupiter.api.Assertions.assertEquals(before + 1, after, "上传后 uploads 目录应多出一个 txt 文件");
        // 媒体列表页仍可正常渲染
        mockMvc.perform(get("/admin/media").session(session))
                .andExpect(status().isOk());
    }

    private long countTxt(Path dir) throws IOException {
        if (!Files.isDirectory(dir)) {
            return 0;
        }
        try (java.util.stream.Stream<Path> s = Files.list(dir)) {
            return s.filter(p -> p.getFileName().toString().endsWith(".txt")).count();
        }
    }

    // ---------------- 附件上传：政策 + 前台下载展示 ----------------

    @Test
    @Order(7)
    void policy_withAttachment_shownOnFrontDetail() throws Exception {
        MockHttpSession session = adminSession();
        String title = "回归测试附件政策-" + UUID.randomUUID().toString().substring(0, 8);
        MockMultipartFile file = new MockMultipartFile("attachment", "test-att.pdf", "application/pdf",
            "fake pdf content".getBytes(StandardCharsets.UTF_8));
        mockMvc.perform(multipart("/admin/policy/add")
                        .file(file)
                        .param("title", title)
                        .param("publish_org", "测试机构")
                        .param("status", "1")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/policy"));

        // 前台详情页应展示附件下载
        Policy latest = policyService.page(1, 15).getList().get(0);
        org.junit.jupiter.api.Assertions.assertEquals(title, latest.getTitle());
        org.junit.jupiter.api.Assertions.assertNotNull(latest.getAttachment());
        mockMvc.perform(get("/policy/detail/" + latest.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("附件下载")));

        // 清理
        mockMvc.perform(post("/admin/policy/delete").param("id", String.valueOf(latest.getId())).session(session))
                .andExpect(status().is3xxRedirection());
    }

    // ---------------- 数据备份 ----------------

    @Test
    @Order(8)
    void backup_createListDelete_works() throws Exception {
        MockHttpSession session = adminSession();
        // 创建备份（注入服务直接生成，避免依赖页面跳转）
        String name = backupService.exportDatabase();
        org.junit.jupiter.api.Assertions.assertTrue(name.endsWith(".sql"));
        // 页面能列出该文件
        mockMvc.perform(get("/admin/backup").session(session))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString(name)));
        // 下载可用
        mockMvc.perform(get("/admin/backup/download").param("filename", name).session(session))
                .andExpect(status().isOk());
        // 删除后列表不再包含
        mockMvc.perform(post("/admin/backup/delete").param("filename", name).session(session))
                .andExpect(status().is3xxRedirection());
        org.junit.jupiter.api.Assertions.assertFalse(backupService.listFiles().stream()
            .anyMatch(f -> f.name().equals(name)));
    }

    // ---------------- 邮件（Spring Boot JavaMailSender） ----------------

    @Test
    @Order(9)
    void sendTestMail_redirectsToSettings() throws Exception {
        MockHttpSession session = adminSession();
        // Mock JavaMailSender 成功发送：应重定向回设置页并提示成功，而不是 500
        mockMvc.perform(post("/admin/settings/sendTestMail").param("testEmail", "test@example.com").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/settings"));
    }

    @Test
    @Order(10)
    void settingsPage_showsMailSection() throws Exception {
        MockHttpSession session = adminSession();
        mockMvc.perform(get("/admin/settings").session(session))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("config"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("邮件服务")));
    }
}
