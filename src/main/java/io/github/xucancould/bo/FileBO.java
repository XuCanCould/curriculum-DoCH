package io.github.xucancould.bo;

import lombok.Data;

/**
 * created by Xu on 2024/3/28 23:36.
 */
@Data
public class FileBO {

    /**
     * 文件 id
     */
    private Integer id;

    /**
     * 文件 key，上传时指定的
     */
    private String key;

    /**
     * 文件路径
     */
    private String path;

    /**
     * 文件 URL
     */
    private String url;
}

