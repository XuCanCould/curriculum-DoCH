package io.github.xucancould.extension.file;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import io.github.xucancould.module.file.AbstractUploader;
import io.github.xucancould.module.file.FileTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayInputStream;

/**
 * created by Xu on 2024/3/30 17:57.
 * 服务端直传
 */
@Slf4j
public class QiniuUploader extends AbstractUploader {

    @Autowired
    private FileProperties fileProperties;

    @Value("${lin.file.qiniuyun.access-key}")
    private String accessKey;

    @Value("${lin.file.qiniuyun.secret-key}")
    private String secretKey;

    @Value("${lin.file.qiniuyun.bucket}")
    private String bucket;

    private UploadManager uploadManager;

    private String upToken;

    public void initUploadManager() {
        Configuration configuration = new Configuration(Region.region2());
        uploadManager = new UploadManager(configuration);
        Auth auth = Auth.create(accessKey, secretKey);
        upToken = auth.uploadToken(bucket);
    }


    @Override
    protected FileProperties getFileProperties() {
        return fileProperties;
    }

    @Override
    protected String getStorePath(String newFilename) {
        return fileProperties.getDomain() + newFilename;
    }

    @Override
    protected String getFileType() {
        return FileTypeEnum.REMOTE.getValue();
    }

    @Override
    protected boolean handleOneFile(byte[] bytes, String newFilename) {
        initUploadManager();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        try {
            Response response = uploadManager.put(inputStream, newFilename, upToken, null, null);
            log.info("qiniuyun upload file response：{}", response.toString());
            return response.isOK();
        } catch (QiniuException e) {
            Response r = e.response;
            log.error("qiniuyun upload file error: {}", r.error);
            return false;
        }
    }

}
