package io.github.xucancould.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.xucancould.model.BookDO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * created by Xu on 2024/3/18 9:08.
 */
@Repository
public interface BookMapper extends BaseMapper<BookDO> {
    List<BookDO> selectByTitleLikeKeyword(String q);
}
