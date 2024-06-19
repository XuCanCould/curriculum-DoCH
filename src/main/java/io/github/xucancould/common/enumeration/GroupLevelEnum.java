package io.github.xucancould.common.enumeration;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * created by Xu on 2024/3/21 20:41.
 */
public enum GroupLevelEnum implements IEnum<Integer> {
    ROOT(1),
    GUEST(2),
    USER(3);

    private final Integer value;

    GroupLevelEnum(Integer value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }
}
