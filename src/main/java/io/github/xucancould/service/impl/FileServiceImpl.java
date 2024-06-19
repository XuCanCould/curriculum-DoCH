package io.github.xucancould.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.xucancould.extension.file.FileProperties;
import io.github.xucancould.bo.FileBO;
import io.github.xucancould.mapper.FileMapper;
import io.github.xucancould.model.FileDO;
import io.github.xucancould.module.file.File;
import io.github.xucancould.module.file.FileTypeEnum;
import io.github.xucancould.module.file.UploadHandler;
import io.github.xucancould.module.file.Uploader;
import io.github.xucancould.service.FileService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * created by Xu on 2024/3/28 23:34.
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, FileDO> implements FileService {

    @Autowired
    private Uploader uploader;


    @Autowired
    private FileProperties fileProperties;


    /**
     * 为什么不做批量插入
     * 1. 批量插入不能得到数据的id字段，不利于直接返回数据
     * 2. 文件上传的数量一般不多，3个左右
     */
    @Override
    public List<FileBO> upload(MultiValueMap<String, MultipartFile> fileMap) {
        ArrayList<FileBO> res = new ArrayList<>();

        uploader.upload(fileMap, new UploadHandler() {
            @Override
            public boolean preHandle(File file) {
                FileDO found = baseMapper.selectByMd5(file.getMd5());
                if (found == null) {
                    return true;
                }
                res.add(transformDoToBo(found, file.getKey()));
                return false;
            }

            @Override
            public void afterHandle(File file) {
                FileDO fileDO = new FileDO();
                BeanUtils.copyProperties(file, fileDO);
                getBaseMapper().insert(fileDO);
                res.add(transformDoToBo(fileDO, file.getKey()));
            }
        });
        return res;
    }

    @Override
    public boolean checkFileExistByMd5(String md5) {
        return this.getBaseMapper().selectCountByMd5(md5) > 0;
    }

    /**
     * 设置文件的 url 和 key
     * @param file
     * @param key
     * @return
     */
    private FileBO transformDoToBo(FileDO file, String key) {
        FileBO fileBO = new FileBO();
        BeanUtils.copyProperties(file, fileBO);
        if (file.getType().equals(FileTypeEnum.LOCAL.getValue())) {
            // 如果是本地文件
            String s = fileProperties.getServePath().split("/")[0];
            if (System.getProperties().getProperty("os.name").toUpperCase().contains("WINDOWS")) {
                // 根据系统平台（Windows或非Windows）将文件路径中的反斜杠替换为斜杠
                fileBO.setUrl(fileProperties.getDomain() + s + "/" + file.getPath().replaceAll("\\\\","/"));
            } else {
                fileBO.setUrl(fileProperties.getDomain() + s + "/" + file.getPath());
            }
        } else {
            fileBO.setUrl(file.getPath());
        }
        fileBO.setKey(key);
        return fileBO;
    }
}
