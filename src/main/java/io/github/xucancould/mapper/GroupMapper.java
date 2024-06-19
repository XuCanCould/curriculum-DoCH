package io.github.xucancould.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.xucancould.model.GroupDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * created by Xu on 2024/3/21 20:38.
 */
@Mapper
public interface GroupMapper extends BaseMapper<GroupDO> {
    /**
     * 获得指定用户的所有分组id
     * @param userId
     * @return
     */
    List<Integer> selectUserGroupIds(@Param("userId") Integer userId);

    List<GroupDO> selectGroupsByUserId(@Param("userId") Integer userId);
}
