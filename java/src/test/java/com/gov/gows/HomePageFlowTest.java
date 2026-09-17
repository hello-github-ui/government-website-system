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
 * 前台公开页面全链路测试：验证首页、公告、政策、裁判文书等页面可正常访问并渲染。
 */
@SpringBootTest
@AutoConfigureMockMvc
class HomePageFlowTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void homeIndex_returns200() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("notices", "policies"));
    }

    @Test
    void noticeList_returns200() throws Exception {
        mockMvc.perform(get("/notice"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("notices"));
    }

    @Test
    void policyList_returns200() throws Exception {
        mockMvc.perform(get("/policy"))
                .andExpect(status().isOk());
    }

    @Test
    void judicialList_returns200() throws Exception {
        mockMvc.perform(get("/judicial"))
                .andExpect(status().isOk());
    }

    @Test
    void judicialSearch_returns200() throws Exception {
        mockMvc.perform(get("/judicial").param("keyword", "案"))
                .andExpect(status().isOk());
    }

    @Test
    void searchAggregate_returns200() throws Exception {
        mockMvc.perform(get("/search").param("q", "公告"))
                .andExpect(status().isOk());
    }

    @Test
    void loginPage_returns200() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    void registerPage_returns200() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk());
    }

    @Test
    void contactPage_returns200() throws Exception {
        mockMvc.perform(get("/contact"))
                .andExpect(status().isOk());
    }

    @Test
    void adminLoginPage_returns200() throws Exception {
        mockMvc.perform(get("/admin/login"))
                .andExpect(status().isOk());
    }

    @Test
    void adminDashboard_requiresLogin_redirects() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/login"));
    }
}
