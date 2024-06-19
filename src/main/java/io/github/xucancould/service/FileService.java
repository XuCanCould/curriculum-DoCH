package io.github.xucancould.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.xucancould.bo.FileBO;
import io.github.xucancould.model.FileDO;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * created by Xu on 2024/3/28 23:33.
 */
public interface FileService extends IService<FileDO> {
    /**
     * 上传文件
     * @param fileMap
     * @return
     */
    List<FileBO> upload(MultiValueMap<String, MultipartFile> fileMap);

    /**
     * 通过md5检查文件是否存在
     * @param md5
     * @return
     */
    boolean checkFileExistByMd5(String md5);
}
