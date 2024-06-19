package io.github.xucancould.common.util;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * created by Xu on 2024/3/30 13:26.
 */
@Slf4j
public class IPUtil {
    private IPUtil() {
        throw new IllegalStateException("Utility class");
    }

    private static final String[] IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };

    public static String getIPFromRequest(HttpServletRequest request) {
        if (request == null) {
            if (null == RequestContextHolder.getRequestAttributes()){
                // 为null意味着无效请求
                return null;
            }
            request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        }

        for(String header : IP_HEADER_CANDIDATES) {
            String ipList = request.getHeader(header);
            if (ipList != null && ipList.length() != 0 && !"unknown".equalsIgnoreCase(ipList)) {
                // 多次反向代理后会有多个ip值，第一个ip才是真实ip
                int index = ipList.indexOf(',');
                if (index != -1) {
                    return ipList.substring(0, index);
                } else {
                    return ipList;
                }
//                更直接的方式，但是不应对不含逗号/只有一个ip的情况
//                log.debug("ipList.split(\",\")[0]::{}", ipList.split(",")[0]);
//                return ipList.split(",")[0];
            }
        }

        log.debug("request.getRemoteAddr()::{}", request.getRemoteAddr());
        return request.getRemoteAddr();
    }

}
