package io.github.xucancould.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.xucancould.model.PermissionDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * created by Xu on 2024/3/19 13:44.
 */
@Mapper
public interface PermissionMapper extends BaseMapper<PermissionDO> {
    /**
     * 通过分组ids得到所有分组下的权限
     *
     * @param groupIds 分组ids
     * @return 权限
     */
    List<PermissionDO> selectPermissionsByGroupIds(@Param("groupIds") List<Integer> groupIds);

    /**
     * 通过分组id得到所有分组下的权限
     *
     * @param groupId 分组id
     * @return 权限
     */
    List<PermissionDO> selectPermissionsByGroupId(@Param("groupId") Integer groupId);
}
