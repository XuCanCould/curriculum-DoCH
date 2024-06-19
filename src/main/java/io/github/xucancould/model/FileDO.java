package io.github.xucancould.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;

/**
 * created by Xu on 2024/3/28 23:32.
 */
@Data
@Builder
@TableName("lin_file")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FileDO extends BaseModel implements Serializable {

    private static final long serialVersionUID = -3203293656352763490L;

    private String path;

    /**
     * LOCAL REMOTE
     */
    private String type;

    private String name;

    private String extension;

    private Integer size;

    /**
     * md5值，防止上传重复文件
     */
    private String md5;
}

