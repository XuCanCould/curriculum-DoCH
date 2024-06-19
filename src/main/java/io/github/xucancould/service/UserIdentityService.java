package io.github.xucancould.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.xucancould.model.UserIdentityDO;

/**
 * created by Xu on 2024/3/22 9:14.
 */
public interface UserIdentityService extends IService<UserIdentityDO> {

    UserIdentityDO createIdentity(Integer userId, String identityType, String username, String credential);

    UserIdentityDO createUsernamePasswordIdentity(Integer userId, String username, String credential);

    /**
     * 使用 用户名-密码 验证用户身份
     * @param userId 用户id
     * @param username 用户名
     * @param password 密码
     * @return
     */
    boolean verifyUsernamePassword(Integer userId, String username, String password);

    /**
     * 修改用户密码
     * @param userId 用户id
     * @param password 密码
     * @return
     */
    boolean changePassword(Integer userId, String password);

    /**
     * 修改用户名称
     * @param userId
     * @param username
     * @return
     */
    boolean changeUsername(Integer userId, String username);

}
