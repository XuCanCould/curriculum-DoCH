package io.github.xucancould.vo;

import io.github.xucancould.model.UserDO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Map;

/**
 * created by Xu on 2024/3/22 16:43.
 * 用户 + 权限 view object
 */
@Data
public class UserPermissionVO {

    private Integer id;

    private String nickname;

    private String avatar;

    private Boolean admin;

    private String email;

    private List<Map<String, List<Map<String, String>>>> permissions;

    public UserPermissionVO() {
    }

    /**
     * 构造函数
     * 这里不可以通过注释@AllArgsConstructor来生成，传递指定UserDO的数据，返回到前端需要通过BeanUtils.copyProperties来复制
     * @param userDO 用户DO
     * @param permissions 权限
     */
    public UserPermissionVO(UserDO userDO, List<Map<String, List<Map<String, String>>>> permissions) {
        BeanUtils.copyProperties(userDO, this);
        this.permissions = permissions;
    }

    public UserPermissionVO(UserDO userDO) {
        BeanUtils.copyProperties(userDO, this);
    }

}
