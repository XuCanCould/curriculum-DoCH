package io.github.xucancould.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.xucancould.model.UserGroupDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * created by Xu on 2024/3/21 21:28.
 */
@Mapper
public interface UserGroupMapper extends BaseMapper<UserGroupDO> {
    /**
     * 批量插入
     * @param relations
     * @return
     */
    int insertBatch(@Param("relations") List<UserGroupDO> relations);
}
