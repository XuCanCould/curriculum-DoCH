package io.github.xucancould.dto.admin;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * created by Xu on 2024/3/28 22:44.
 */
@Data
public class RemovePermissionsDTO {
    @Positive(message = "{group.id.positive}")
    @NotBlank(message = "{group.id.not-null}")
    private Integer groupId;

    private List<Integer> permissionIds;
}
