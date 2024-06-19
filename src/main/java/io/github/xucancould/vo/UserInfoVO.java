package io.github.xucancould.vo;

import io.github.xucancould.model.GroupDO;
import io.github.xucancould.model.UserDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * created by Xu on 2024/3/22 23:44.
 * 用户信息 view object
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoVO {

    private Integer id;

    /**
     * 用户名，唯一
     */
    private String username;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 头像url
     */
    private String avatar;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 分组
     */
    private List<GroupDO> groups;

    public UserInfoVO(UserDO user, List<GroupDO> groups) {
        BeanUtils.copyProperties(user, this);
        this.groups = groups;
    }

}
