package io.github.xucancould.module.file;

import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * created by Xu on 2024/3/29 9:12.
 */
public interface Uploader {
    List<File> upload(MultiValueMap<String, MultipartFile> fileMap);

    List<File> upload(MultiValueMap<String, MultipartFile> fileMap, UploadHandler uploadHandler);
}
