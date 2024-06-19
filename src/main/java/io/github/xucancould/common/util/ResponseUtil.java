package io.github.xucancould.common.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;

/**
 * created by Xu on 2024/3/26 14:53.
 */
public class ResponseUtil {
    private ResponseUtil() {
    }

    /**
     * 获得当前响应
     * @return
     */
    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    /**
     * 设置当前响应的http状态码
     * @param status
     */
    public static void setCurrentResponseHttpStatus(int status) {
        getResponse().setStatus(status);
    }

    /**
     * 在原本的工具类中还提供了
     * 生成CreatedResponse/DeletedResponse/UpdatedResponse的
     * 方法
     */
}
