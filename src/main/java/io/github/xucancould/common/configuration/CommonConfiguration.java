package io.github.xucancould.common.configuration;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.github.talelin.autoconfigure.bean.PermissionMetaCollector;
import io.github.xucancould.common.interception.RequestLogInterceptor;
import io.github.xucancould.module.log.MDCAccessServletFilter;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * created by Xu on 2024/3/24 14:34.
 */

@Configuration(proxyBeanMethods = false)
public class CommonConfiguration {
    @Bean
    public RequestLogInterceptor requestLogInterceptor() {
        return new RequestLogInterceptor();
    }

    /**
     * 记录每个被 @PermissionMeta 记录的信息，在beans的后置调用
     *
     * @return PermissionMetaCollector
     */
    @Bean
    public PermissionMetaCollector postProcessBeans() {
        return new PermissionMetaCollector();
    }

    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则
     * 一缓： Mybatis 的一级缓存，它是 SqlSession 级别的缓存，在同一个 SqlSession 生命周期内，执行相同的 SQL 语句会优先从缓存中获取结果，避免了对数据库的重复查询。
     * 二缓： Mybatis 的二级缓存，它是全局的跨 SqlSession 的缓存，允许在不同 SqlSession 之间共享数据。
     * @return
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * 提供 sql 注入功能
     * @return
     */
    @Bean
    public ISqlInjector sqlInjector() {
        return new DefaultSqlInjector();
    }

    /**
     * 配置序列化时的 jackson （对象 -> json）
     * 实现自动转化：驼峰转为下划线
     * @return
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customJackson() {
        return jacksonObjectMapperBuilder -> {
            jacksonObjectMapperBuilder.failOnUnknownProperties(false);
            jacksonObjectMapperBuilder.propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        };
    }

    /**
     * 将 request 相关信息（如请求 url）放入 MDC(Mapped Diagnostic Context) 中供日志使用
     * @return
     */
    @Bean
    public FilterRegistrationBean<MDCAccessServletFilter> mdcAccessServletFilter() {
        FilterRegistrationBean<MDCAccessServletFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new MDCAccessServletFilter());
        registrationBean.setName("mdc-access-servlet-filter");
        return registrationBean;
    }

}
