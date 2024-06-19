package io.github.xucancould.common.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * created by Xu on 2024/3/25 9:29.
 */
@SuppressWarnings("ConfigurationProperties")
@Component
@ConfigurationProperties
@PropertySource(value = {"classpath:code-message.properties"}, encoding = "UTF-8")
public class CodeMessageConfiguration {
    private static Map<Integer, String> codeMessage;

    public static String getMessage(Integer code) {
        return codeMessage.get(code);
    }

    public static Map<Integer, String> getCodeMessage(Integer code, String message) {
        return codeMessage;
    }

    /**
     * 在初始化后，读取到的配置文件数据通过这个方法注入到 codeMessage 中
     * @param codeMessage
     */
    public void setCodeMessage(Map<Integer, String> codeMessage) {
        CodeMessageConfiguration.codeMessage = codeMessage;
    }

}
