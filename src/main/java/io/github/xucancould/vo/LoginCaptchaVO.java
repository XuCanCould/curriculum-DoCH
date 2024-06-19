package io.github.xucancould.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * created by Xu on 2024/3/20 21:41.
 * value object
 * 登录验证码视图对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginCaptchaVO {
    /**
     * 加密后的验证码
     */
    private String tag;
    /**
     * 验证码图片地址，可使用base64
     */
    private String image;
}
