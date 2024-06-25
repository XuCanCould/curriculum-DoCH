package io.github.xucancould.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * created by Xu on 2024/3/28 21:35.
 */
@Data
@TableName("lin_group_permission")
public class GroupPermissionDO extends BaseModel implements Serializable {

    private static final long serialVersionUID = -358487811336536495L;


    /**
     * 分组id
     */
    private Integer groupId;

    /**
     * 权限id
     */
    private Integer permissionId;

    public GroupPermissionDO(Integer groupId, Integer permissionId) {
        this.groupId = groupId;
        this.permissionId = permissionId;
    }

}
