package io.github.xucancould.common.listener;

import io.github.talelin.autoconfigure.bean.PermissionMetaCollector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * created by Xu on 2024/3/19 13:22.
 * 处理报错：
 * Field permissionMetaCollector in io.github.talelin.autoconfigure.interceptor.AuthorizeInterceptor required a bean of type 'io.github.talelin.autoconfigure.bean.PermissionMetaCollector' that could not be found.
 * 属性 io.github.talelin.autoconfigure.interceptor.AuthorizeInterceptor 的 permissionMetaCollector 需要这个类型的bean，但没有被发现
 * 解决：
 */
@Configuration
public class PermissionConfig {
    @Bean
    public PermissionMetaCollector permissionMetaCollector() {
        return new PermissionMetaCollector();
    }
}
