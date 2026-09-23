package com.example.gov;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 政府官网系统主启动类。
 *
 * <p>由原 PHP 项目重构而来，技术栈为 Spring Boot 3 + MyBatis + Thymeleaf，
 * 采用前后端不分离（服务端渲染）模式。</p>
 *
 * @author gows
 */
@SpringBootApplication
@MapperScan("com.example.gov.mapper")
public class GovApplication {

    public static void main(String[] args) {
        SpringApplication.run(GovApplication.class, args);
    }
}
