package io.github.xucancould.common.enumeration;

import com.baomidou.mybatisplus.annotation.IEnum;
import io.github.talelin.autoconfigure.exception.ParameterException;

/**
 * created by Xu on 2024/6/14 10:09.
 */
public enum DigitalModeEnum implements IEnum<Integer> {
    CONSERVATION_RESTORATION(1, "数字化采集与保护修复"),
    CULTURAL_HERITAGE(2, "数字文创"),
    EXHIBIT(3, "数字陈展");

    private final Integer value;
    private final String description;

    DigitalModeEnum(Integer value, String description) {
        this.value = null;
        this.description = null;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    public String getDescription() {
        return this.description;
    }

    public static String getDescriptionByValue(Integer value) {
        for (DigitalModeEnum type : DigitalModeEnum.values()) {
            if (type.getValue().equals(value)) {
                return type.getDescription();
            }
        }
        throw new ParameterException(12300);
    }
}
