package io.github.xucancould.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.xucancould.dto.admin.*;
import io.github.xucancould.model.GroupDO;
import io.github.xucancould.bo.GroupPermissionBO;
import io.github.xucancould.model.PermissionDO;
import io.github.xucancould.model.UserDO;

import java.util.List;
import java.util.Map;

/**
 * created by Xu on 2024/3/23 21:31.
 */
public interface AdminService {
    /**
     * 删除多个权限
     * @param dto
     * @return
     */
    boolean removePermissions(RemovePermissionsDTO dto);

    /**
     * 分配多个权限
     * @param dto
     * @return
     */
    boolean dispatchPermissions(DispatchPermissionsDTO dto);

    /**
     * 分配单个权限
     * @param dto
     * @return
     */
    boolean dispatchPermission(DispatchPermissionDTO dto);

    /**
     * 删除分组
     * @param id
     * @return
     */
    boolean deleteGroup(Integer id);

    /**
     * 更新组群信息
     * @param id
     * @param dto
     * @return
     */
    boolean updateGroup(Integer id, UpdateGroupDTO dto);

    /**
     * 创建组群。需要关联权限和组群id
     * @param dto
     * @return
     */
    boolean createGroup(NewGroupDTO dto);

    /**
     * 通过id获得组群
     * @param id
     * @return
     */
    GroupPermissionBO getGroup(Integer id);

    /**
     * 获得用户分页信息
     * @param page
     * @param count
     * @return
     */
    IPage<GroupDO> getGroupPage(Integer page, Integer count);

    /**
     * 删除用户
     * 处理基本的用户信息、身份信息，还有组群信息
     * @param id
     * @return
     */
    boolean deleteUser(Integer id);

    /**
     * 管理员更新用户信息
     * 需要细致地处理组群信息
     * @param id
     * @param dto
     * @return
     */
    boolean updateUserInfo(Integer id, UpdateUserInfoDTO dto);

    /**
     * 管理员修改用户密码
     * @param id
     * @param dto
     * @return
     */
    boolean changeUserPassword(Integer id, ResetPasswordDTO dto);

    /**
     * 获得结构化的权限信息
     * @return
     */
    Map<String, List<PermissionDO>> getAllStructuralPermissions();

    /**
     * 获得所有权限
     * @return
     */
    List<PermissionDO> getAllPermissions();

    /**
     * 根据分组id查询分页用户
     * @param groupId
     * @param count
     * @param page
     * @return
     */
    IPage<UserDO> getUserPageByGroupId(Integer groupId, Integer count, Integer page);

    /**
     * 获得全部的分组
     * @return
     */
    List<GroupDO> getAllGroups();
}
