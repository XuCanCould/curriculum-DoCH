package io.github.xucancould.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * created by Xu on 2024/3/21 17:09.
 * 登录验证码BO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginCaptchaBO {
    private String captcha;
    private Long expired;
}

