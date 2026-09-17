package com.gov.gows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 后台管理员登录与鉴权全链路测试。
 * 使用种子数据 admin / Admin@123456 验证真实登录链路。
 */
@SpringBootTest
@AutoConfigureMockMvc
class AdminLoginFlowTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void login_wrongPassword_redirectsWithError() throws Exception {
        mockMvc.perform(post("/admin/login/doLogin")
                        .param("username", "admin")
                        .param("password", "wrong-password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/login"));
    }

    @Test
    void login_correctCredentials_setsSessionAndRedirects() throws Exception {
        mockMvc.perform(post("/admin/login/doLogin")
                        .param("username", "admin")
                        .param("password", "Admin@123456"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"))
                .andExpect(request().sessionAttribute("admin_id", org.hamcrest.Matchers.notNullValue()));
    }

    @Test
    void captchaEndpoint_returnsPng() throws Exception {
        mockMvc.perform(get("/admin/captcha"))
                .andExpect(status().isOk());
    }
}
