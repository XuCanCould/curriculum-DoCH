package io.github.xucancould.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.xucancould.common.mybatis.LinPage;
import io.github.xucancould.model.LogDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;

/**
 * created by Xu on 2024/3/23 11:39.
 */
@Mapper
public interface LogMapper extends BaseMapper<LogDO> {
    /**
     * 查询用户名分页列表
     * @param pager
     * @return
     */
    IPage<String> getUserNames(LinPage<LogDO> pager);

    IPage<LogDO> searchLogsByUsernameAndKeywordAndRange(LinPage<LogDO> pager, String name, String keyword, Date start, Date end);

    /**
     * 查询日志
     * @param pager
     * @param name
     * @param start
     * @param end
     * @return
     */
    IPage<LogDO> findLogsByUsernameAndRange(LinPage<LogDO> pager, String name, Date start, Date end);

}
