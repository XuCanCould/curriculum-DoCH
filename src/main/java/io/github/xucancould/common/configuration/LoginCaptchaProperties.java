package io.github.xucancould.common.configuration;

import io.github.xucancould.common.util.CaptchaUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * created by Xu on 2024/3/20 21:44.
 * 登录图形验证码配置类
 */
@Slf4j
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "login-captcha")
public class LoginCaptchaProperties {
    // 密钥
    private String secret = CaptchaUtil.getRandomString(32);
    // 偏移量
    private String iv = CaptchaUtil.getRandomString(16);
    // 启用验证码
    private Boolean enabled = Boolean.FALSE;

    public void setSecret(String secret) {
        final long ivLen1 = 16;
        final long ivLen2 = 24;
        final long ivLen3 = 32;
        if (StringUtils.hasText(secret)) {
            byte[] bytes = secret.getBytes();
            if (bytes.length == ivLen1 || bytes.length == ivLen2 || bytes.length == ivLen3) {
                this.secret = secret;
            } else {
                log.warn("AES密钥必须为128/192/256bit，输入的密钥为{}bit，已启用随机密钥{}", bytes.length * 8, this.secret);
            }
        }
    }

    /**
     * 设置AES加密的初始向量，
     * @param iv
     */
    public void setIv(String iv) {
        final long ivLen = 16;
//  hasText: return str != null && !str.isEmpty() && containsText(str);
        if (StringUtils.hasText(iv)) {
            byte[] bytes = iv.getBytes();
            if (bytes.length == ivLen) {
                this.iv = iv;
            } else {
                log.warn("AES初始向量必须为128bit，输入的密钥为{}bit，已启用随机向量{}", bytes.length * 8, this.iv);
            }
        }
    }
}
