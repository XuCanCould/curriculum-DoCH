package io.github.xucancould.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * created by Xu on 2024/3/21 21:12.
 */
@Data
@TableName("lin_user_group")
public class UserGroupDO implements Serializable {

    private static final long serialVersionUID = -7219009955825484511L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private Integer groupId;

    public UserGroupDO(Integer userId, Integer groupId) {
        this.userId = userId;
        this.groupId = groupId;
    }
}
