package io.github.xucancould.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.talelin.autoconfigure.exception.ForbiddenException;
import io.github.xucancould.model.GroupDO;
import io.github.xucancould.service.GroupService;
import io.github.xucancould.bo.GroupPermissionBO;
import io.github.xucancould.common.enumeration.GroupLevelEnum;
import io.github.xucancould.common.mybatis.LinPage;
import io.github.xucancould.mapper.GroupMapper;
import io.github.xucancould.mapper.UserGroupMapper;
import io.github.xucancould.model.PermissionDO;
import io.github.xucancould.model.UserGroupDO;
import io.github.xucancould.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * created by Xu on 2024/3/21 20:46.
 */
@Service
public class  GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {
    @Autowired
    private UserGroupMapper userGroupMapper;

    @Autowired
    private PermissionService permissionService;

    @Override
    public boolean checkGroupExistByName(String name) {
        QueryWrapper<GroupDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(GroupDO::getName, name);
        return this.baseMapper.selectCount(wrapper) > 0;
    }

    @Override
    public GroupPermissionBO getGroupAndPermissions(Integer id) {
        GroupDO group = this.baseMapper.selectById(id);
        List<PermissionDO> permissions = permissionService.getPermissionByGroupId(id);
        return new GroupPermissionBO(group, permissions);
    }

    @Override
    public IPage<GroupDO> getGroupPage(int page, int count) {
        LinPage<GroupDO> pager = new LinPage<>(page, count);
        return this.baseMapper.selectPage(pager, null);
    }

    @Override
    public boolean addUserGroupRelations(Integer userId, List<Integer> addIds) {
        if (addIds == null || addIds.isEmpty()) {
            return true;
        }
        if (checkIsRootByUserId(userId)) {
            throw new ForbiddenException(10077);
        }
        List<UserGroupDO> relations = addIds.stream().map(id -> new UserGroupDO(userId, id)).collect(Collectors.toList());
        return userGroupMapper.insertBatch(relations) > 0;
    }

    @Override
    public boolean deleteUserGroupRelations(Integer userId, List<Integer> deleteIds) {
        if (deleteIds == null || deleteIds.isEmpty()) {
            return true;
        }
        if (checkIsRootByUserId(userId)) {
            throw new ForbiddenException(10078);
        }
        QueryWrapper<UserGroupDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserGroupDO::getUserId, userId).in(UserGroupDO::getGroupId, deleteIds);
        return userGroupMapper.delete(wrapper) > 0;
    }

    /**
     * 通过族群id获取其所有的用户id
     * @param id
     * @return
     */
    @Override
    public List<Integer> getGroupUserIds(Integer id) {
        QueryWrapper<UserGroupDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserGroupDO::getGroupId, id);
        List<UserGroupDO> userGroupDOS = userGroupMapper.selectList(wrapper);
        return userGroupDOS.stream().map(UserGroupDO::getUserId).collect(Collectors.toList());
    }

    @Override
    public List<Integer> getUserGroupIdsByUserId(Integer userId) {
        return this.baseMapper.selectUserGroupIds(userId);
    }

    @Override
    public List<GroupDO> getUserGroupsByUserId(Integer userId) {
        return this.baseMapper.selectGroupsByUserId(userId);
    }

    @Override
    public boolean checkIsRootByUserId(Integer userId) {
        QueryWrapper<UserGroupDO> wrapper = new QueryWrapper<>();

        wrapper.lambda().eq(UserGroupDO::getUserId, userId)
                .eq(UserGroupDO::getGroupId, this.getParticularGroupIdByLevel(GroupLevelEnum.ROOT));
        UserGroupDO userGroupDO = userGroupMapper.selectOne(wrapper);
        return userGroupDO != null;
    }

    @Override
    public GroupDO getParticularGroupByLevel(GroupLevelEnum level) {
        if (GroupLevelEnum.USER == level) {
            return null;
        } else {
            QueryWrapper<GroupDO> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(GroupDO::getLevel, level.getValue());
            return this.baseMapper.selectOne(wrapper);
        }
    }

    @Override
    public Integer getParticularGroupIdByLevel(GroupLevelEnum level) {
        GroupDO group = this.getParticularGroupByLevel(level);
        return group == null ? 0 : group.getId();
    }

    @Override
    public boolean checkGroupExistById(int id) {
        QueryWrapper<GroupDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(GroupDO::getId, id);
        return this.baseMapper.selectCount(wrapper) > 0;
    }
}
