package io.github.xucancould.module.file;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * created by Xu on 2024/3/29 9:21.
 */
public enum FileTypeEnum implements IEnum<String> {
    LOCAL("LOCAL"),
    // 远程文件，存云端，如OSS
    REMOTE("REMOTE");

    final String value;

    FileTypeEnum(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
