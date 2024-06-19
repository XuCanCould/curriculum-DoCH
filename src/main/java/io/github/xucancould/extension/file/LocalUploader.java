package io.github.xucancould.extension.file;

import io.github.xucancould.module.file.AbstractUploader;
import io.github.xucancould.module.file.FileTypeEnum;
import io.github.xucancould.module.file.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.BufferedOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * created by Xu on 2024/3/30 15:37.
 */
@Slf4j
public class LocalUploader extends AbstractUploader {

    @Autowired
    private FileProperties fileProperties;

    @PostConstruct
    public void init() {
        FileUtil.initStoreDir(fileProperties.getStoreDir());
    }

    @Override
    protected String getStorePath(String newFilename) {
        Date date = new Date();
        String format = new SimpleDateFormat("yyyy/MM/dd").format(date);
        Path absolutePath = Paths.get(fileProperties.getStoreDir(), format).toAbsolutePath();
        File file = new File(absolutePath.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        return Paths.get(format, newFilename).toString();
    }

    @Override
    protected String getFileType() {
        return FileTypeEnum.LOCAL.getValue();
    }

    @Override
    protected boolean handleOneFile(byte[] bytes, String newFilename) {
        String absolutePath =
                FileUtil.getFileAbsolutePath(fileProperties.getStoreDir(), getStorePath(newFilename));
        try (BufferedOutputStream stream =
                     new BufferedOutputStream(Files.newOutputStream(new File(absolutePath).toPath()))) {
            stream.write(bytes);
        } catch (Exception e) {
            log.error("write file to local err:", e);
            return false;
        }
        return true;
    }

    @Override
    protected FileProperties getFileProperties() {
        return fileProperties;
    }
}
