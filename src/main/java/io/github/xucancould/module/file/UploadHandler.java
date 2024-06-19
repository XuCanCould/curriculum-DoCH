package io.github.xucancould.module.file;

/**
 * created by Xu on 2024/3/29 9:15.
 */
public interface UploadHandler {
    /**
     * 预处理(在上传到云/存储到本地之前)
     * 当数据库中不存在，返回true
     * @param file
     * @return
     */
    boolean preHandle(File file);

    /**
     * 后处理(在上传到云/存储到本地之后)
     * 将图片信息保存到数据库中
     * @param file
     * @return
     */
    void afterHandle(File file);
}
