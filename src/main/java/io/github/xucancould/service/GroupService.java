package io.github.xucancould.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import io.github.xucancould.model.GroupDO;
import io.github.xucancould.bo.GroupPermissionBO;
import io.github.xucancould.common.enumeration.GroupLevelEnum;

import java.util.List;

/**
 * created by Xu on 2024/3/21 20:45.
 */
public interface GroupService extends IService<GroupDO> {

    /**
     * 通过分组名称检查分组是否存在
     * @param name
     * @return
     */
    boolean checkGroupExistByName(String name);

    /**
     * 获得分组及权限
     * @param id
     * @return
     */
    GroupPermissionBO getGroupAndPermissions(Integer id);

    /**
     * 分页获取分组数据
     * @param page
     * @param count
     * @return
     */
    IPage<GroupDO> getGroupPage(int page, int count);

    boolean addUserGroupRelations(Integer userId, List<Integer> addIds);

    /**
     * 删除用户组关联
     * @param userId
     * @param deleteIds
     * @return
     */
    boolean deleteUserGroupRelations(Integer userId, List<Integer> deleteIds);

    /**
     * 获取当前组的所有用户id
     * @param id
     * @return
     */
    List<Integer> getGroupUserIds(Integer id);

    /**
     * 获取用户所在组
     * @param userId
     * @return
     */
    List<Integer> getUserGroupIdsByUserId(Integer userId);


    /**
     * 获得用户的所有分组
     *
     * @param userId 用户id
     * @return 所有分组
     */
    List<GroupDO> getUserGroupsByUserId(Integer userId);


    /**
     * 检查该用户是否是root
     * @param userId
     * @return
     */
    boolean checkIsRootByUserId(Integer userId);

    /**
     * 通过分组级别获取超级管理员分组或者游客分组
     * @param level
     * @return
     */
    GroupDO getParticularGroupByLevel(GroupLevelEnum level);

    /**
     * 通过分组级别获取超级管理员分组或游客分组的id
     * @param level
     * @return
     */
    Integer getParticularGroupIdByLevel(GroupLevelEnum level);

    /**
     * 检查分组是否存在
     * @param id
     * @return
     */
    boolean checkGroupExistById(int id);
}
