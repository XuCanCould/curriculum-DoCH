package io.github.xucancould.module.file;

import io.github.talelin.autoconfigure.exception.*;
import io.github.xucancould.extension.file.FileProperties;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * created by Xu on 2024/3/29 9:33.
 */
public abstract class AbstractUploader implements Uploader {

    private UploadHandler uploadHandler;

    @Override
    public List<File> upload(MultiValueMap<String, MultipartFile> fileMap) {
        checkFileMap(fileMap);
        return handleMultipartFiles(fileMap);
    }

    @Override
    public List<File> upload(MultiValueMap<String, MultipartFile> fileMap, UploadHandler uploadHandler) {
        this.uploadHandler = uploadHandler;
        return this.upload(fileMap);
    }


    /**
     * 处理上传的多个文件，筛选出符合要求的文件，并通过列表返回。
     * 先获取单个文件的大小限制，再遍历文件，调用handleOneFile0将符合要求的文件加入到res中。
     * @param fileMap
     * @return 符合要求的文件的列表
     */
    protected List<File> handleMultipartFiles(MultiValueMap<String, MultipartFile> fileMap) {
        long singleFileLimit = getSingleFileLimit();
        ArrayList<File> res = new ArrayList<>();
        fileMap.keySet().forEach(key -> fileMap.get(key).forEach(file -> {
            if (!file.isEmpty()) {
                handleOneFile0(res, singleFileLimit, file);
            }
        }));
        return res;
    }

    private void handleOneFile0(List<File> res, long singleFileLimit, MultipartFile file) {
        byte[] bytes = getFileBytes(file);
        String[] include = getFileProperties().getInclude();
        String[] exclude = getFileProperties().getExclude();
        String ext = checkOneFile(include, exclude, singleFileLimit, file.getOriginalFilename(), res.size());
        String newFilename = getNewFilename(ext);
        String storePath = getStorePath(newFilename);
        String md5 = FileUtil.getFileMD5(bytes);
        File fileData = File.builder().
                name(newFilename).
                md5(md5).
                key(file.getName()).
                path(storePath).
                size(bytes.length).
                type(getFileType()).
                extension(ext).
                build();
        // 如果预处理器不为空，且处理结果为false，直接返回, 否则处理
        if (uploadHandler != null && !uploadHandler.preHandle(fileData)) {
            return;
        }
        boolean handled = handleOneFile(bytes, newFilename);
        if (handled) {
            res.add(fileData);
            // 上传到本地或云上成功之后，调用afterHandle
            if (uploadHandler != null) {
                uploadHandler.afterHandle(fileData);
            }
        }
    }

    /**
     * 获取单个文件的大小限制
     * @return
     */
    private long getSingleFileLimit() {
        String singleLimit = getFileProperties().getSingleLimit();
        return FileUtil.parseSize(singleLimit);
    }

    /**
     * 获取文件字节数组
     * @param file
     * @return
     */
    protected byte[] getFileBytes(MultipartFile file) {
        byte[] bytes;
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            throw new FailedException(10190, "read file date failed");
        }
        return bytes;
    }


    /**
     * 检查文件是否为空
     * 提交的文件数量是否超过允许的最大文件数
     * @param fileMap
     */
    protected void checkFileMap(MultiValueMap<String, MultipartFile> fileMap) {
        if (fileMap == null || fileMap.isEmpty()) {
            throw new NotFoundException(10026);
        }
        int nums = getFileProperties().getNums();
        AtomicInteger sizes = new AtomicInteger();
        fileMap.keySet().forEach(key -> fileMap.get(key).forEach(file -> {
            if (!file.isEmpty()) {
                sizes.getAndIncrement();
            }
        }));
        if (sizes.get() > nums) {
            throw new FileTooManyException(10121);
        }
    }

    /**
     * 获取新的文件名
     * 使用UUID并且去掉 -
     * @param ext 后缀
     * @return 新的文件名
     */
    protected String getNewFilename(String ext) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid + ext;
    }


    /**
     * 检查文件后缀
     * @param include 多个文件后缀的数组
     * @param exclude
     * @param ext
     * @return
     */
    protected boolean checkExt(String[] include, String[] exclude, String ext) {
        int inLen = include == null ? 0 : include.length;
        int exLen = exclude == null ? 0 : exclude.length;
        // 如果两者都有取 include，有一者则用一者
        if (inLen > 0 && exLen > 0) {
            return this.findInInclude(include, ext);
        } else if (inLen > 0) {
            // 有include，无exclude
            return this.findInInclude(include, ext);
        } else if (exLen > 0) {
            // 有exclude，无include
            return this.findInExclude(exclude, ext);
        } else {
            // 二者都没有
            return true;
        }
    }

    protected boolean findInInclude(String[] include, String ext) {
        for (String s : include) {
            if (ext.equals(s)) {
                return true;
            }
        }
        return false;
    }

    protected boolean findInExclude(String[] exclude, String ext) {
        for (String s : exclude) {
            if (s.equals(ext)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 单个文件的检查
     * @param include
     * @param exclude
     * @param singleFileLimit
     * @param originName
     * @param length
     */
    protected String checkOneFile(String[] include, String[] exclude, long singleFileLimit, String originName, int length) {
        // 检查文件后缀
        String fileExt = FileUtil.getFileExt(originName);
        if (!this.checkExt(include, exclude, fileExt)) {
            throw new FileExtensionException(fileExt + "文件类型不支持");
        }
        // 检查文件大小
        if (singleFileLimit < length) {
            throw new FileTooLargeException(originName + "文件不能超过" + singleFileLimit);
        }
        return "";
    }

    /**
     * 获取文件存储路径
     * 提供给云/本地存储的路径方法
     * @param newFilename
     * @return
     */
    protected abstract String getStorePath(String newFilename);

    /**
     * 返回文件存储位置类型
     * @return LOCAL ｜ REMOTE
     */
    protected abstract String getFileType();

    /**
     * 处理单个文件
     * @param bytes
     * @param newFilename
     * @return
     */
    protected abstract boolean handleOneFile(byte[] bytes, String newFilename);

    protected abstract FileProperties getFileProperties();
}
