package io.github.xucancould.common.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.xucancould.vo.PageResponseVO;

import java.util.List;

/**
 * created by Xu on 2024/3/26 15:51.
 * 分页工具类，将查询数据库查询结果封装成分页的视图对象
 */
public class PageUtil {
    private PageUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> PageResponseVO<T> Build(IPage<T> iPage) {
        return new PageResponseVO<>(Math.toIntExact(iPage.getTotal()), iPage.getRecords(),
                Math.toIntExact(iPage.getCurrent()), Math.toIntExact(iPage.getSize()));
    }

    public static <K, T> PageResponseVO<K> Build(IPage<T> iPage, List<K> records) {
        return new PageResponseVO<>(Math.toIntExact(iPage.getTotal()), records,
                Math.toIntExact(iPage.getCurrent()), Math.toIntExact(iPage.getSize()));
    }
}
