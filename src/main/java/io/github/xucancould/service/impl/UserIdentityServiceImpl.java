package io.github.xucancould.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.talelin.core.util.EncryptUtil;
import io.github.xucancould.common.constant.IdentityConstant;
import io.github.xucancould.mapper.UserIdentityMapper;
import io.github.xucancould.model.UserIdentityDO;
import io.github.xucancould.service.UserIdentityService;
import org.springframework.stereotype.Service;

/**
 * created by Xu on 2024/3/22 9:15.
 */
@Service
public class UserIdentityServiceImpl extends ServiceImpl<UserIdentityMapper, UserIdentityDO> implements UserIdentityService {


    @Override
    public UserIdentityDO createIdentity(Integer userId, String identityType, String username, String credential) {
        UserIdentityDO userIdentityDO = new UserIdentityDO();
        userIdentityDO.setCredential(credential);
        userIdentityDO.setUserId(userId);
        userIdentityDO.setIdentifier(username);
        userIdentityDO.setIdentityType(identityType);
        this.baseMapper.insert(userIdentityDO);
        return userIdentityDO;
    }

    @Override
    public UserIdentityDO createUsernamePasswordIdentity(Integer userId, String username, String credential) {
        // 密码加密
        credential = EncryptUtil.encrypt(credential);
        return this.createIdentity(userId, IdentityConstant.USERNAME_PASSWORD_IDENTITY, username, credential);
    }

    /**
     * 验证用户身份
     * eq：用户名密码、验证方式，EncryptUtil验证未加密/已加密的密码
     * @param userId 用户id
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @Override
    public boolean verifyUsernamePassword(Integer userId, String username, String password) {
        QueryWrapper<UserIdentityDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserIdentityDO::getUserId, userId)
                .eq(UserIdentityDO::getIdentifier, username)
                .eq(UserIdentityDO::getIdentityType, IdentityConstant.USERNAME_PASSWORD_IDENTITY);
        UserIdentityDO userIdentity = this.baseMapper.selectOne(wrapper);
        return EncryptUtil.verify(userIdentity.getCredential(), password);
    }

    public boolean changePassword(Integer userId, String password) {
        String encrypted = EncryptUtil.encrypt(password);
        UserIdentityDO userIdentity = new UserIdentityDO().builder().credential(encrypted).build();
        QueryWrapper<UserIdentityDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserIdentityDO::getUserId, userId);
        return this.baseMapper.update(userIdentity, wrapper) > 0;
    }

    public boolean changeUsername(Integer userId, String username) {
        UserIdentityDO userIdentity = UserIdentityDO.builder().identifier(username).build();
        QueryWrapper<UserIdentityDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserIdentityDO::getUserId, userId);
        return this.baseMapper.update(userIdentity, wrapper) > 0;
    }
}
