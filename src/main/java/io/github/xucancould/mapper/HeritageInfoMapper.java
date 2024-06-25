package io.github.xucancould.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.xucancould.model.HeritageInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * created by Xu on 2024/6/14 10:25.
 */
@Mapper
public interface HeritageInfoMapper extends BaseMapper<HeritageInfoDO> {
    int selectCountById(@Param("id") Integer id);
}
