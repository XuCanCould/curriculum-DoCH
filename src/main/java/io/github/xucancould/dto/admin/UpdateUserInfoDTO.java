package io.github.xucancould.dto.admin;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * created by Xu on 2024/3/27 11:24.
 * 管理员更新用户信息的数据传输对象
 */
@Data
public class UpdateUserInfoDTO {
    @NotEmpty(message = "{group.ids.not-empty}")
    private List<@Min(1) Integer> groupIds;
}
