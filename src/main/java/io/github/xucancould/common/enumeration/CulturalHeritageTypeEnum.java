package io.github.xucancould.common.enumeration;

import com.baomidou.mybatisplus.annotation.IEnum;
import io.github.talelin.autoconfigure.exception.ParameterException;

/**
 * created by Xu on 2024/6/14 9:20.
 */
public enum CulturalHeritageTypeEnum implements IEnum<Integer> {
    FOLK_LITERATURE(1, "民间文学"),
    TRADITIONAL_MUSIC(2, "传统音乐"),
    TRADITIONAL_DANCE(3, "传统舞蹈"),
    TRADITIONAL_DRAMA(4, "传统戏剧"),
    QUYI(5, "曲艺"),
    TRADITIONAL_SPORTS_GAMES_ACROBATICS(6, "传统体育、游艺与杂技"),
    TRADITIONAL_ART(7, "传统美术"),
    TRADITIONAL_CRAFTSMANSHIP(8, "传统技艺"),
    TRADITIONAL_MEDICINE(9, "传统医药"),
    FOLK_CUSTOMS(10, "民俗");

    private final Integer value;
    private final String description;

    CulturalHeritageTypeEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    public String getDescription() {
        return this.description;
    }

    public static String getDescriptionByValue(Integer value) {
        for (CulturalHeritageTypeEnum type : CulturalHeritageTypeEnum.values()) {
            if (type.getValue().equals(value)) {
                return type.getDescription();
            }
        }
        throw new ParameterException(12300);
    }
}
