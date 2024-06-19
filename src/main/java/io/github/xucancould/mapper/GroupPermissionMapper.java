package io.github.xucancould.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.xucancould.model.GroupPermissionDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * created by Xu on 2024/3/28 21:37.
 */
@Mapper
public interface GroupPermissionMapper extends BaseMapper<GroupPermissionDO> {
    /**
     * 分组权限关系集合，
     * @param relations
     * @return
     */
    int insertBatch(@Param("relations") List<GroupPermissionDO> relations);

    int deleteBatchByGroupIdAndPermissionId(@Param("groupId") Integer groupId, @Param("permissionId") List<Integer> permissionIds);
}
