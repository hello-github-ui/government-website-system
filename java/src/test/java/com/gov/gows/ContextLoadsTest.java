package com.gov.gows;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 应用上下文加载冒烟测试：验证 Spring 容器、数据源、MyBatis、Redis 装配无误。
 */
@SpringBootTest
class ContextLoadsTest {

    @Test
    void contextLoads() {
        // 上下文成功加载即通过
    }
}
