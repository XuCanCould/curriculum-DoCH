package io.github.xucancould.common.mybatis;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * created by Xu on 2024/3/26 9:20.
 * 分页对象，为保持多端的一致性，重写 MyBatis-Plus 分页对象，将起始页从 1 改为 0
 */
public class LinPage<T> extends Page<T> {
    private static final long serialVersionUID = -2183463672525305273L;

    public LinPage() {
        super.setCurrent(0);
    }

    public LinPage(int current, int size) {
        this(current, size, 0);
    }

    public LinPage(int current, int size, int total) {
        this(current, size, total, true);
    }

    public LinPage(int current, int size, int total, boolean isSearchCount) {
        super(current, size, total, isSearchCount);
        if (current < 0) {
            current = 0;
        }
        super.setCurrent(current);
    }


    @Override
    public boolean hasPrevious() {
        return super.getCurrent() > 0;
    }

    @Override
    public boolean hasNext() {
        return super.getCurrent() + 1 < this.getPages();
    }

    /**
     * 重写计算偏移量，将分页从第 0 开始
     *
     * @return 偏移量
     */
    @Override
    public long offset() {
        return getCurrent() > 0 ? super.getCurrent() * getSize() : 0;
    }

}
