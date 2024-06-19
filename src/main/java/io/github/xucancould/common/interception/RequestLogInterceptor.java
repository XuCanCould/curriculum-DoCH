package io.github.xucancould.common.interception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * created by Xu on 2024/3/24 14:35.
 * 请求日志拦截器
 */
@Slf4j
public class RequestLogInterceptor implements AsyncHandlerInterceptor {

    private final ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        startTime.set(System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("[{}] -> [{}] from: {} costs: {}ms",
                request.getMethod(),
                request.getRequestURI(),
                request.getRemoteAddr(),
                System.currentTimeMillis() - startTime.get());
        startTime.remove();
    }
}
