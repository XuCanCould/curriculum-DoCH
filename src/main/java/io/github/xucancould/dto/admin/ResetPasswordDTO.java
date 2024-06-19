package io.github.xucancould.dto.admin;

import io.github.talelin.autoconfigure.validator.EqualField;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * created by Xu on 2024/3/27 10:18.
 * 重置密码的数据传输对象
 */
@EqualField(srcField = "newPassword", dstField = "confirmPassword", message = "{password.equal-field}")
@Data
public class ResetPasswordDTO {
    @NotBlank(message = "{password.new.not-blank}")
    @Pattern(regexp = "^[A-Za-z0-9_*&$#@]{6,22}$", message = "{password.new.pattern}")
    private String newPassword;

    @NotBlank(message = "{password.confirm.not-blank}")
    private String confirmPassword;
}
