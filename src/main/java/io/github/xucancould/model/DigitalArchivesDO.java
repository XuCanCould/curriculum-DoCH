package io.github.xucancould.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

/**
 * created by Xu on 2024/6/14 10:18.
 * 数字化档案
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("digital_archives")
@EqualsAndHashCode(callSuper = true)
public class DigitalArchivesDO extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    // 关联文化遗产记录id
    private Integer heritageId;

    private String archiveName;

    // 档案类型-DigitalModeEnum
    private String archiveType;

    private String archiveImage;

    private String archivePath;

}