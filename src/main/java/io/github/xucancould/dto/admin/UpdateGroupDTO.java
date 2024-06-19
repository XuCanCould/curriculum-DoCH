package io.github.xucancould.dto.admin;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * created by Xu on 2024/3/28 23:06.
 * 分组数据更新的传输对象
 */
@Data
public class UpdateGroupDTO {
    @NotBlank(message = "{group.name.not-blank}")
    @Length(min = 1, max = 60, message = "{group.name.length}")
    private String name;

    @Length(max = 255, message = "{group.info.length}")
    private String info;
}
