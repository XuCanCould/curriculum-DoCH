package io.github.xucancould.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import io.github.xucancould.common.mybatis.LinPage;
import io.github.xucancould.dto.user.ChangePasswordDTO;
import io.github.xucancould.dto.user.RegisterDTO;
import io.github.xucancould.dto.user.UpdateInfoDTO;
import io.github.xucancould.model.PermissionDO;
import io.github.xucancould.model.UserDO;
import io.github.xucancould.vo.LoginCaptchaVO;

import java.awt.*;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

/**
 * created by Xu on 2024/3/21 17:18.
 */
public interface UserService extends IService<UserDO> {

    /**
     * 通过用户id检查用户是否存在
     * @param id
     * @return
     */
    boolean checkUserExistById(Integer id);

    /**
     * 生成无状态的登录验证码
     * @return
     * @throws IOException
     * @throws FontFormatException
     * @throws GeneralSecurityException
     */
    LoginCaptchaVO generateCaptcha() throws IOException, FontFormatException, GeneralSecurityException;

    /**
     * 验证验证码
     * @param captcha
     * @param tag
     * @return
     */
    boolean verifyCaptcha(String captcha, String tag);

    /**
     * 通过用户名得到用户
     * @param username 用户名
     * @return
     */
    UserDO getUserByUsername(String username);


    /**
     * 获取用户权限
     * @param userId 用户ID
     * @return
     */
    List<Map<String, List<Map<String, String>>>> getStructuralUserPermissions(Integer userId);

    /**
     * 获取用户权限
     * 查找用户搜索分组，查找分组下的所有权限
     * @param userId 用户ID
     * @return
     */
    List<PermissionDO> getUserPermissions(Integer userId);


    /**
     * 修改用户密码
     * @param validator 新的密码信息
     * @return UserDO 被修改密码的用户
     */
    UserDO changeUserPassword(ChangePasswordDTO validator);

    /**
     * 更新用户信息
     * @param validator
     * @return
     */
    UserDO updateUserInfo(UpdateInfoDTO validator);

    /**
     * 检查用户是否存在
     * @param username 用户名
     * @return
     */
    boolean checkUserExistByUsername(String username);

    /**
     * 获取超级管理员ID
     * @return
     */
    Integer getRootUserId();

    IPage<UserDO> getUserPageByGroupId(LinPage<UserDO> pager, Integer groupId);

    /**
     * 创建用户
     * @param dto
     * @return
     */
    UserDO createUser(RegisterDTO dto);
}
