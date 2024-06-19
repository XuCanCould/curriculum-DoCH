package io.github.xucancould.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.talelin.autoconfigure.exception.FailedException;
import io.github.talelin.autoconfigure.exception.ForbiddenException;
import io.github.talelin.autoconfigure.exception.NotFoundException;
import io.github.talelin.autoconfigure.exception.ParameterException;
import io.github.xucancould.common.enumeration.GroupLevelEnum;
import io.github.xucancould.common.util.BeanCopyUtil;
import io.github.xucancould.model.GroupDO;
import io.github.xucancould.service.GroupService;
import io.github.xucancould.service.UserIdentityService;
import io.github.xucancould.bo.LoginCaptchaBO;
import io.github.xucancould.common.LocalUser;
import io.github.xucancould.common.configuration.LoginCaptchaProperties;
import io.github.xucancould.common.mybatis.LinPage;
import io.github.xucancould.common.util.CaptchaUtil;
import io.github.xucancould.dto.user.ChangePasswordDTO;
import io.github.xucancould.dto.user.RegisterDTO;
import io.github.xucancould.dto.user.UpdateInfoDTO;
import io.github.xucancould.mapper.UserGroupMapper;
import io.github.xucancould.mapper.UserMapper;
import io.github.xucancould.model.PermissionDO;
import io.github.xucancould.model.UserDO;
import io.github.xucancould.model.UserGroupDO;
import io.github.xucancould.service.UserService;
import io.github.xucancould.vo.LoginCaptchaVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.awt.*;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * created by Xu on 2024/3/21 17:18.
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    @Autowired
    private LoginCaptchaProperties captchaConfig;

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserIdentityService userIdentityService;

    @Autowired
    private UserGroupMapper userGroupMapper;

    @Override
    public boolean checkUserExistById(Integer id) {
        int rows = this.baseMapper.selectCountById(id);
        return rows > 0;
    }

    @Override
    public LoginCaptchaVO generateCaptcha() throws IOException, FontFormatException, GeneralSecurityException {
        String code = CaptchaUtil.getRandomString(CaptchaUtil.RANDOM_STR_NUM);
        String base64String = CaptchaUtil.getRandomCodeBase64(code);
        String tag = CaptchaUtil.getTag(code, captchaConfig.getSecret(), captchaConfig.getIv());
        return new LoginCaptchaVO(tag, "data:image/png;base64," + base64String);
    }

    /**
     * 验证返回的验证码是否正确
     * 解码得到 LoginCaptchaBO 对象，忽略大小写并且判断是否过期
     * @param captcha
     * @param tag
     * @return
     */
    @Override
    public boolean verifyCaptcha(String captcha, String tag) {
        try {
            LoginCaptchaBO loginCaptchaBO = CaptchaUtil.decodeTag(captchaConfig.getSecret(), captchaConfig.getIv(), tag);
            return captcha.equalsIgnoreCase(loginCaptchaBO.getCaptcha()) || System.currentTimeMillis() > loginCaptchaBO.getExpired();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 根据用户名获取用户信息
     * 调用这个对象（UserServiceImpl）提供的 getOne 方法：this.getOne(wrapper);
     * @param username 用户名
     * @return 用户
     */
    @Override
    public UserDO getUserByUsername(String username) {
        QueryWrapper<UserDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserDO::getUsername, username);
        return this.getOne(wrapper);
    }

    @Override
    public List<PermissionDO> getUserPermissions(Integer userId) {
        groupService.getUserGroupIdsByUserId(userId);
        return null;
    }

    @Override
    public List<Map<String, List<Map<String, String>>>> getStructuralUserPermissions(Integer userId) {
        return null;
    }

    public UserDO changeUserPassword(ChangePasswordDTO validator) {
        UserDO user = LocalUser.getLocalUser();
        boolean valid = userIdentityService.verifyUsernamePassword(user.getId(), user.getUsername(), validator.getOldPassword());
        if (!valid) {
            throw new ParameterException(10032);
        }
        valid = userIdentityService.changePassword(user.getId(), validator.getNewPassword());
        if (!valid) {
            throw new FailedException(10011);
        }
        return user;
    }

    public UserDO updateUserInfo(UpdateInfoDTO validator) {
        UserDO localUser = LocalUser.getLocalUser();
        if (StringUtils.hasText(validator.getEmail())) {
            if (!validator.getEmail().equals(localUser.getEmail())) {
                boolean valid = this.checkUserExistByUsername(localUser.getUsername());
                if (!valid) {
                    throw new FailedException(10011);
                }
                boolean changeSuccess = userIdentityService.changeUsername(localUser.getId(), validator.getEmail());
                if (changeSuccess) {
                    localUser.setEmail(validator.getEmail());
                }
            }
        }
        BeanCopyUtil.copyNonNullProperties(validator, localUser);
        return localUser;
    }

    public boolean checkUserExistByUsername(String username) {
        return this.baseMapper.selectCountByUsername(username) > 0;
    }

    @Override
    public Integer getRootUserId() {
        Integer rootGroupId = groupService.getParticularGroupIdByLevel(GroupLevelEnum.ROOT);
        UserGroupDO userGroupDO = null;
        if (rootGroupId != null) {
            QueryWrapper<UserGroupDO> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(UserGroupDO::getGroupId, rootGroupId);
            userGroupDO = userGroupMapper.selectOne(wrapper);
        }
        return userGroupDO == null ? 0 : userGroupDO.getUserId();
    }

    @Override
    public IPage<UserDO> getUserPageByGroupId(LinPage<UserDO> pager, Integer groupId) {
        Integer rootGroupId = groupService.getParticularGroupIdByLevel(GroupLevelEnum.ROOT);
        return this.baseMapper.selectPageByGroupId(pager, groupId, rootGroupId);
    }

    @Override
    public UserDO createUser(RegisterDTO dto) {
        boolean exist = this.checkUserExistByUsername(dto.getUsername());
        if (!exist) {
            throw new ForbiddenException(10071);
        }
        if (StringUtils.hasText(dto.getEmail())) {
            exist = this.checkUserExistByEmail(dto.getEmail());
            if (exist) {
                throw new ForbiddenException(10076);
            }
        } else {
            dto.setEmail(null);
        }
        UserDO user = new UserDO();
        BeanCopyUtil.copyNonNullProperties(dto, user);
        this.baseMapper.insert(user);
        if (dto.getGroupIds() != null && !dto.getGroupIds().isEmpty()) {
            checkGroupsValid(dto.getGroupIds());
            checkGroupsExist(dto.getGroupIds());
            List<UserGroupDO> userGroupDOList = dto.getGroupIds().stream()
                    .map(groupId -> new UserGroupDO(user.getId(), groupId)).collect(Collectors.toList());
            userGroupMapper.insertBatch(userGroupDOList);
        } else {
            Integer guestId = groupService.getParticularGroupIdByLevel(GroupLevelEnum.GUEST);
            UserGroupDO userGroupDO = new UserGroupDO(user.getId(), guestId);
            userGroupMapper.insert(userGroupDO);
        }
        userIdentityService.createUsernamePasswordIdentity(user.getId(), dto.getUsername(), dto.getPassword());
        return user;
    }

    private boolean checkUserExistByEmail(String email) {
        LambdaQueryWrapper<UserDO> emailEq = new QueryWrapper<UserDO>()
                .lambda()
                .eq(UserDO::getEmail, email);
        return this.baseMapper.selectCount(emailEq) > 0;
    }

    private void checkGroupsValid(List<Integer> ids) {
        GroupDO root = groupService.getParticularGroupByLevel(GroupLevelEnum.ROOT);
        boolean anyMatch = ids.stream().anyMatch(id -> id.equals(root.getId()));
        if (anyMatch) {
            throw new ForbiddenException(10073);
        }
    }

    private void checkGroupsExist(List<Integer> ids) {
        ids.forEach(id -> {
            boolean exist = groupService.checkGroupExistById(id);
            if (!exist) {
                throw new NotFoundException(10023);
            }
        });
    }
}
