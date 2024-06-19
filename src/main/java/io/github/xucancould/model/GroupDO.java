package io.github.xucancould.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.xucancould.common.enumeration.GroupLevelEnum;
import lombok.*;

import java.io.Serializable;

/**
 * created by Xu on 2024/3/21 20:38.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("lin_group")
@EqualsAndHashCode(callSuper = true)
public class GroupDO extends BaseModel implements Serializable {
    private static final long serialVersionUID = -8994898895671436007L;

    private String name;

    private String info;

    @TableField(value = "`level`")
    private GroupLevelEnum level;

}
