package io.github.xucancould.module.log;

import io.github.xucancould.common.util.IPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * created by Xu on 2024/3/29 17:19.
 */
public class MDCAccessServletFilter implements Filter {

    private static Logger log = LoggerFactory.getLogger(MDCAccessServletFilter.class);

    /**
     * 先将请求与响应的信息放入MDC中，将数据输出到日志，然后清除MDC中的数据
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        putRequestMDC(servletRequest);
        try {
            filterChain.doFilter(servletRequest, servletResponse);
            putResponseMDC(servletResponse);
            accessLog();
        } finally {
            clearMDC();
        }
    }

    public void putRequestMDC(ServletRequest servletRequest) {
        MDC.put(MDCAccessConstant.REQUEST_REMOTE_HOST_MDC_KEY, servletRequest.getRemoteHost());
        MDC.put(MDCAccessConstant.REQUEST_PROTOCOL_MDC_KEY, servletRequest.getProtocol());
        MDC.put(MDCAccessConstant.REQUEST_REMOTE_ADDR_MDC_KEY, servletRequest.getRemoteAddr());
        MDC.put(MDCAccessConstant.REQUEST_REMOTE_PORT_MDC_KEY, String.valueOf(servletRequest.getRemotePort()));
        MDC.put(MDCAccessConstant.REQUEST_BODY_BYTES_SENT_MDC_KEY, String.valueOf(servletRequest.getContentLength()));

        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            MDC.put(MDCAccessConstant.REQUEST_REMOTE_ADDR_MDC_KEY, IPUtil.getIPFromRequest(httpServletRequest));
            MDC.put(MDCAccessConstant.REQUEST_REQUEST_URI_MDC_KEY, httpServletRequest.getRequestURI());
            StringBuffer requestUrl = httpServletRequest.getRequestURL();
            if (requestUrl != null) {
                MDC.put(MDCAccessConstant.REQUEST_REQUEST_URL_MDC_KEY, requestUrl.toString());
            }
            MDC.put(MDCAccessConstant.REQUEST_METHOD_MDC_KEY, httpServletRequest.getMethod());
            MDC.put(MDCAccessConstant.REQUEST_QUERY_STRING_MDC_KEY, httpServletRequest.getQueryString());
            MDC.put(MDCAccessConstant.REQUEST_USER_AGENT_MDC_KEY, httpServletRequest.getHeader("User-Agent"));
            MDC.put(MDCAccessConstant.REQUEST_X_FORWARDED_FOR_MDC_KEY, httpServletRequest.getHeader("X-Forwarded-For"));
            MDC.put(MDCAccessConstant.REQUEST_REFERER_MDC_KEY, httpServletRequest.getHeader("referer"));
        }
    }

    public void putResponseMDC(ServletResponse servletResponse) {
        if (servletResponse instanceof HttpServletResponse) {
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            MDC.put(MDCAccessConstant.RESPONSE_STATUS_MDC_KEY, String.valueOf(httpServletResponse.getStatus()));
        }
    }

    public void accessLog() {
        // TODO 输出日志
        log.info("");
    }

    public void clearMDC() {
        MDC.remove(MDCAccessConstant.REQUEST_REMOTE_HOST_MDC_KEY);
        MDC.remove(MDCAccessConstant.REQUEST_PROTOCOL_MDC_KEY);
        MDC.remove(MDCAccessConstant.REQUEST_REMOTE_ADDR_MDC_KEY);
        MDC.remove(MDCAccessConstant.REQUEST_REMOTE_PORT_MDC_KEY);
        MDC.remove(MDCAccessConstant.REQUEST_BODY_BYTES_SENT_MDC_KEY);

        MDC.remove(MDCAccessConstant.REQUEST_METHOD_MDC_KEY);
        MDC.remove(MDCAccessConstant.REQUEST_QUERY_STRING_MDC_KEY);
        MDC.remove(MDCAccessConstant.REQUEST_USER_AGENT_MDC_KEY);
        MDC.remove(MDCAccessConstant.REQUEST_X_FORWARDED_FOR_MDC_KEY);
        MDC.remove(MDCAccessConstant.REQUEST_REFERER_MDC_KEY);

        MDC.remove(MDCAccessConstant.REQUEST_REQUEST_URI_MDC_KEY);
        MDC.remove(MDCAccessConstant.REQUEST_REQUEST_URL_MDC_KEY);
        MDC.remove(MDCAccessConstant.RESPONSE_STATUS_MDC_KEY);
    }
}
