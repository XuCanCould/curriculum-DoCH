package io.github.xucancould.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.talelin.autoconfigure.exception.ForbiddenException;
import io.github.talelin.autoconfigure.exception.NotFoundException;
import io.github.xucancould.common.enumeration.GroupLevelEnum;
import io.github.xucancould.dto.admin.*;
import io.github.xucancould.model.*;
import io.github.xucancould.service.*;
import io.github.xucancould.bo.GroupPermissionBO;
import io.github.xucancould.common.mybatis.LinPage;
import io.github.xucancould.mapper.GroupPermissionMapper;
import io.github.xucancould.mapper.UserGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * created by Xu on 2024/3/23 21:32.
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserGroupMapper userGroupMapper;

    @Autowired
    private UserIdentityService userIdentityService;

    @Autowired
    private GroupPermissionMapper groupPermissionMapper;

    @Override
    public boolean removePermissions(RemovePermissionsDTO dto) {
        groupPermissionMapper.deleteBatchByGroupIdAndPermissionId(dto.getGroupId(), dto.getPermissionIds());
        return false;
    }

    @Override
    public boolean dispatchPermissions(DispatchPermissionsDTO dto) {
        List<GroupPermissionDO> collect = dto.getPermissionIds().stream()
                .map(permissionId -> new GroupPermissionDO(dto.getGroupId(), permissionId))
                .collect(Collectors.toList());
        return groupPermissionMapper.insertBatch(collect) > 0;
    }

    @Override
    public boolean dispatchPermission(DispatchPermissionDTO dto) {
        GroupPermissionDO groupPermission = new GroupPermissionDO(dto.getGroupId(), dto.getPermissionId());
        return groupPermissionMapper.insert(groupPermission) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteGroup(Integer id) {
        // 不允许删除root和guest组
        Integer rootGroupId = groupService.getParticularGroupIdByLevel(GroupLevelEnum.ROOT);
        Integer guestGroupId = groupService.getParticularGroupIdByLevel(GroupLevelEnum.GUEST);
        if (id.equals(rootGroupId)) {
            throw new ForbiddenException(10074);
        }
        if (id.equals(guestGroupId)) {
            throw new ForbiddenException(10075);
        }
        throwGroupNotExistById(id);
        List<Integer> groupUserIds = groupService.getGroupUserIds(id);
        if(!groupUserIds.isEmpty()) {
            throw new ForbiddenException(10027);
        }
        // 删除 权限-分组 数据
        groupPermissionMapper.delete(new QueryWrapper<GroupPermissionDO>().eq("group_id", id));
        return groupService.removeById(id);
    }

    @Override
    public boolean updateGroup(Integer id, UpdateGroupDTO dto) {
//        throwGroupNameExist(dto.getName());
        GroupDO exist = groupService.getById(id);
        if (exist == null) {
            throw new NotFoundException(10024);
        }
        if (!exist.getName().equals(dto.getName())) {
            throwGroupNameExist(dto.getName());
        }
        GroupDO group = GroupDO.builder().name(dto.getName()).info(dto.getInfo()).build();
        group.setId(id);
        return groupService.updateById(group);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean createGroup(NewGroupDTO dto) {
        throwGroupNameExist(dto.getName());
        GroupDO group = GroupDO.builder().name(dto.getName()).info(dto.getInfo()).build();
        groupService.save(group);
        if (dto.getPermissionIds() != null && dto.getPermissionIds().size() > 0) {
            List<GroupPermissionDO> relations = dto.getPermissionIds().stream()
                    .map(id -> new GroupPermissionDO(group.getId(), id))
                    .collect(Collectors.toList());
            groupPermissionMapper.insertBatch(relations);
        }
        return true;
    }

    private void throwGroupNameExist(String groupName) {
        boolean existByName = groupService.checkGroupExistByName(groupName);
        if (existByName) {
            throw new ForbiddenException(10072);
        }
    }

    @Override
    public GroupPermissionBO getGroup(Integer id) {
        throwGroupNotExistById(id);
        return groupService.getGroupAndPermissions(id);
    }

    @Override
    public IPage<GroupDO> getGroupPage(Integer page, Integer count) {
        return groupService.getGroupPage(page, count);
    }

    /**
     * 判断用户是否为超级超级管理员
     * userService移除，userGroupService移除
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteUser(Integer id) {
        throwUserNotExistById(id);
        if (userService.getRootUserId().equals(id)) {
            throw new ForbiddenException(10079);
        }
        boolean userRemoved = userService.removeById(id);
        QueryWrapper<UserGroupDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserGroupDO::getUserId, id);
        boolean groupRemoved = userGroupMapper.deleteById(id) > 0;
        return userRemoved && groupRemoved && userIdentityService.removeById(id);
    }

    @Override
    public boolean updateUserInfo(Integer id, UpdateUserInfoDTO dto) {
        List<Integer> groupIds = dto.getGroupIds();
        GroupDO root = groupService.getParticularGroupByLevel(GroupLevelEnum.ROOT);
        boolean anyMatch = groupIds.stream().anyMatch(it -> it.equals(root));
        if (anyMatch) {
            throw new ForbiddenException(10073);
        }
        List<Integer> dbGroupId = groupService.getUserGroupIdsByUserId(id);
        List<Integer> deletedGroupId = dbGroupId.stream().filter(it -> !groupIds.contains(it)).collect(Collectors.toList());
        List<Integer> addGroupIds = groupIds.stream().filter(it -> !dbGroupId.contains(it)).collect(Collectors.toList());
        return groupService.deleteUserGroupRelations(id, deletedGroupId) && groupService.addUserGroupRelations(id, addGroupIds);
    }

    @Override
    public boolean changeUserPassword(Integer id, ResetPasswordDTO dto) {
        throwUserNotExistById(id);
        return userIdentityService.changePassword(id, dto.getNewPassword());
    }

    private void throwUserNotExistById(Integer id) {
        boolean exist = userService.checkUserExistById(id);
        if (!exist) {
            throw new NotFoundException(10021);
        }
    }

    @Override
    public Map<String, List<PermissionDO>> getAllStructuralPermissions() {
        List<PermissionDO> allPermissions = getAllPermissions();
        HashMap<String, List<PermissionDO>> map = new HashMap<>();
        allPermissions.forEach(permission -> {
            if(map.containsKey(permission.getModule())) {
                map.get(permission.getModule()).add(permission);
            } else {
                List<PermissionDO> permissions = new ArrayList<>();
                permissions.add(permission);
                map.put(permission.getModule(), permissions);
            }
        });
        return map;
    }

    @Override
    public List<PermissionDO> getAllPermissions() {
        QueryWrapper<PermissionDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PermissionDO::getMount, true);
        return permissionService.list(wrapper);
    }

    @Override
    public IPage getUserPageByGroupId(Integer groupId, Integer count, Integer page) {
        LinPage<UserDO> pager = new LinPage<>(page, count);
        IPage<UserDO> iPage;
        // 如果group_id为空，则以分页的形式返回所有（非根用户的）用户
        if (groupId == null) {
            Integer rootUserId = userService.getRootUserId();
            QueryWrapper<UserDO> wrapper = new QueryWrapper<>();
            wrapper.lambda().ne(UserDO::getId, rootUserId);
            iPage = userService.page(pager, wrapper);
        } else {
            iPage = userService.getUserPageByGroupId(pager, groupId);
        }
        return iPage;
    }

    @Override
    public List<GroupDO> getAllGroups() {
        Integer rootGroupId = groupService.getParticularGroupIdByLevel(GroupLevelEnum.ROOT);
        QueryWrapper<GroupDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().ne(GroupDO::getId, rootGroupId);
        return groupService.list(wrapper);
    }

    private void throwGroupNotExistById(Integer id) {
        boolean exist = groupService.checkGroupExistById(id);
        if (!exist) {
            throw new NotFoundException(10024);
        }
    }
}
