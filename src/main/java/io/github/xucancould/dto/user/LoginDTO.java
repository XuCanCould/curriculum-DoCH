package io.github.xucancould.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * created by Xu on 2024/3/22 8:30.
 * 登录的数据传输对象
 */
@Data
@NoArgsConstructor
public class LoginDTO {
    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    private String captcha;
}
