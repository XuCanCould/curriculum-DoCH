package io.github.xucancould.dto.user;

import io.github.talelin.autoconfigure.validator.EqualField;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * created by Xu on 2024/3/23 14:40.
 */
@Data
@NoArgsConstructor
@EqualField(srcField = "newPassword", dstField = "confirmPassword", message = "{password.equal-field}")
public class ChangePasswordDTO {
    @NotBlank(message = "旧密码不能为空")
    String oldPassword;

    @NotBlank(message = "新密码不能为空")
    String newPassword;

    @NotBlank(message = "确认密码不能为空")
    String confirmPassword;
}