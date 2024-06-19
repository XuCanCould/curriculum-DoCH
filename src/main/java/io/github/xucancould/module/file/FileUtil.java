package io.github.xucancould.module.file;

import org.springframework.util.DigestUtils;
import org.springframework.util.unit.DataSize;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * created by Xu on 2024/3/29 13:27.
 */
public class FileUtil {
    private FileUtil() {}

    public static FileSystem getDefaultFileSystem() {
        return FileSystems.getDefault();
    }

    public static boolean isAbsolutePath(String str) {
        Path path = getDefaultFileSystem().getPath(str);
        return path.isAbsolute();
    }

    public static void initStoreDir(String dir) {
        String absDir;
        if (isAbsolutePath(dir)) {
            absDir = dir;
        } else {
            String cmd = getCmd();
            Path path = getDefaultFileSystem().getPath(cmd, dir);
            absDir = path.toAbsolutePath().toString();
        }
        File file = new File(absDir);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static String getCmd() {
        return System.getProperty("user.dir");
    }

    public static Long parseSize(String size) {
        DataSize singleLimitData = DataSize.parse(size);
        return singleLimitData.toBytes();
    }

    /**
     * 获取文件的MD5
     * @param bytes
     * @return
     */
    public static String getFileMD5(byte[] bytes) {
        return DigestUtils.md5DigestAsHex(bytes);
    }

    /**
     * 得到文件的后缀
     * @param filename
     * @return
     */
    public static String getFileExt(String filename) {
        int index = filename.lastIndexOf('.');
        return filename.substring(index);
    }

    public static String getFileAbsolutePath(String dir, String filename) {
        if (isAbsolutePath(dir)) {
            return getDefaultFileSystem().getPath(dir, filename).toAbsolutePath().toString();
        } else {
            return getDefaultFileSystem().getPath(getCmd(), dir, filename).toAbsolutePath().toString();
        }
    }
}
