package io.github.xucancould.dto.user;

import io.github.talelin.autoconfigure.validator.EqualField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;

/**
 * created by Xu on 2024/3/23 18:16.
 */
@Data
@NoArgsConstructor
public class UpdateInfoDTO {
    @Email(message = "{email}")
    private String email;

    @Length(min = 2, max = 10, message = "{nickname.length}")
    private String nickname;

    @Length(min = 2, max = 10, message = "{username.length}")
    private String username;

    @Length(max = 500, message = "{avatar.length}")
    private String avatar;
}
