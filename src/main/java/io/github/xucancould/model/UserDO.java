package io.github.xucancould.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;

/**
 * created by Xu on 2024/3/18 10:17.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("lin_user")
@EqualsAndHashCode(callSuper = true)
public class UserDO extends BaseModel implements Serializable {
    private static final long serialVersionUID = -1463999384554707735L;

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
}
