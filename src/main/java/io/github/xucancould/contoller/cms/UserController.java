package io.github.xucancould.contoller.cms;

import io.github.talelin.autoconfigure.exception.ParameterException;
import io.github.talelin.core.annotation.AdminRequired;
import io.github.talelin.core.annotation.LoginRequired;
import io.github.talelin.core.token.DoubleJWT;
import io.github.talelin.core.token.Tokens;
import io.github.xucancould.common.LocalUser;
import io.github.xucancould.common.configuration.LoginCaptchaProperties;
import io.github.xucancould.dto.user.ChangePasswordDTO;
import io.github.xucancould.dto.user.LoginDTO;
import io.github.xucancould.dto.user.RegisterDTO;
import io.github.xucancould.dto.user.UpdateInfoDTO;
import io.github.xucancould.model.GroupDO;
import io.github.xucancould.service.GroupService;
import io.github.xucancould.service.UserIdentityService;
import io.github.xucancould.service.UserService;
import io.github.xucancould.vo.*;
import io.github.xucancould.model.UserDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * created by Xu on 2024/3/18 10:58.
 */
@RestController
@RequestMapping("/cms/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserIdentityService userIdentityService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private LoginCaptchaProperties captchaConfig;

    @Autowired
    private DoubleJWT jwt;

    @PostMapping("/captcha")
    public LoginCaptchaVO userCaptcha() throws Exception {
        if (Boolean.TRUE.equals(captchaConfig.getEnabled())) {
            return userService.generateCaptcha();
        }
        return new LoginCaptchaVO();
    }

    @PostMapping("register")
    @AdminRequired
    public CreatedVo userRegister(@RequestBody @Validated RegisterDTO validator) {
        userService.createUser(validator);
        return new CreatedVo(11);
    }

    @PostMapping("/login")
    public Tokens userLogin(@RequestBody @Validated LoginDTO loginDTO, @RequestHeader(required = false) String tag) {
        // 如果适用验证码登录
        if (Boolean.TRUE.equals(captchaConfig.getEnabled())) {
            if (!StringUtils.hasText(loginDTO.getCaptcha()) || !StringUtils.hasText(tag)) {
                throw  new ParameterException(10260);
            }
            if (!userService.verifyCaptcha(loginDTO.getCaptcha(), tag)) {
                throw new ParameterException(10260);
            }
        }
        UserDO user = userService.getUserByUsername(loginDTO.getUsername());
        boolean valid = userIdentityService.verifyUsernamePassword(
                user.getId(), user.getUsername(),
                loginDTO.getPassword());
        if (!valid) {
            throw new ParameterException(10031);
        }
        LocalUser.setLocalUser(user);
        return jwt.generateTokens(user.getId());
    }

    /**
     * 获取用户权限
     * @return
     */
    @GetMapping("/permissions")
    @LoginRequired
    public UserPermissionVO getPermissions() {
        UserDO localUser = LocalUser.getLocalUser();
        boolean admin = groupService.checkIsRootByUserId(localUser.getId());
        List<Map<String, List<Map<String, String>>>> permissions = userService.getStructuralUserPermissions(localUser.getId());
        UserPermissionVO userPermissions = new UserPermissionVO(localUser, permissions);
        userPermissions.setAdmin(admin);
        return userPermissions;
    }

    /**
     * 获取用户信息
     * @return
     */
    @GetMapping("/information")
    @LoginRequired
    public UserInfoVO getUserInfo() {
        UserDO user = LocalUser.getLocalUser();
        List<GroupDO> groups = groupService.getUserGroupsByUserId(user.getId());
        return new UserInfoVO(user, groups);
    }

    /**
     * 修改密码
     * @param validator
     */
    @PutMapping("/change_password")
    @LoginRequired
    public UpdatedVO changePassword(@RequestBody @Validated ChangePasswordDTO validator) {
        UserDO userDO = userService.changeUserPassword(validator);
        return new UpdatedVO(4);
    }

    /**
     * 刷新令牌
     * @return
     */
    @GetMapping("/refresh")
    @LoginRequired
    public Tokens refresh() {
        UserDO userDO = LocalUser.getLocalUser();
        return jwt.generateTokens(userDO.getId());
    }

    /**
     * 更新用户信息
     * @param userDO
     * @return
     */
    @PutMapping
    @LoginRequired
    public UpdatedVO update(@RequestBody @Validated UpdateInfoDTO userDO) {
        userService.updateUserInfo(userDO);
        return new UpdatedVO(6);
    }

}
