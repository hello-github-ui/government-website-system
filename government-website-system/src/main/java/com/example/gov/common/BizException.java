package com.example.gov.common;

import lombok.Getter;

/**
 * 业务异常：用于在控制器/服务层抛出，由全局异常处理器统一转换为提示或错误页。
 */
@Getter
public class BizException extends RuntimeException {

    private final int code;

    public BizException(String message) {
        super(message);
        this.code = 400;
    }

    public BizException(int code, String message) {
        super(message);
        this.code = code;
    }
}
