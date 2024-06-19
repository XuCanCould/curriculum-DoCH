package io.github.xucancould.common.configuration;

import io.github.talelin.autoconfigure.interceptor.AuthorizeInterceptor;
import io.github.talelin.autoconfigure.interceptor.LogInterceptor;
import io.github.xucancould.common.interception.RequestLogInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * created by Xu on 2024/3/21 17:26.
 */
@Configuration(proxyBeanMethods = false)
@Slf4j
public class WebConfiguration implements WebMvcConfigurer {
    @Value("${auth.enabled:false}")
    private boolean authEnabled;

    @Value("${request-log.enabled:false}")
    private boolean requestLogEnabled;

    @Value("${lin.file.store-dir:assets/}")
    private String dir;

    @Value("${lin.file.serve-path:assets/**}")
    private String servePath;

    @Autowired
    private AuthorizeInterceptor authorizeInterceptor;

    @Autowired
    private RequestLogInterceptor requestLogInterceptor;

    @Autowired
    private LogInterceptor logInterceptor;

    /**
     * 跨域配置
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
//        WebMvcConfigurer.super.addCorsMappings(registry);
        registry.addMapping("/**") // 配置搭配所有路径
                .allowedOriginPatterns("*") // 允许跨域的域名，可以用正则表达式（*表示所有域名）
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true) // 需要携带凭证
                .maxAge(3600)   // 浏览器对特定资源的请求结果的缓存时间
                .allowedHeaders("*");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (authEnabled) {
            //开发环境忽略签名认证
            registry.addInterceptor(authorizeInterceptor)
                    .excludePathPatterns(getDirServePath());
        }
        if (requestLogEnabled) {
            registry.addInterceptor(requestLogInterceptor);
        }
        registry.addInterceptor(logInterceptor);
    }

    private String getDirServePath() {
        return servePath;
    }
}
