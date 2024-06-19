package io.github.xucancould.dto.admin;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * created by Xu on 2024/4/21 20:02.
 */
@Data
public class DispatchPermissionsDTO {
    @Positive(message = "{group.id.positive}")
    @NotNull(message = "{group.id.not-null}")
    private Integer groupId;

    private List<Integer> permissionIds;
}
