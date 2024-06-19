package io.github.xucancould.extension.file;

import io.github.xucancould.common.factory.YamlPropertySourceFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * created by Xu on 2024/3/30 14:58.
 */
@Component
@ConfigurationProperties(prefix = "lin.file")
@PropertySource(value = "classpath:file-config.yml", encoding = "UTF-8",
    factory = YamlPropertySourceFactory.class)
// 指定配置文件，由于配置文件不在resource下，所以需要使用YamlPropertySourceFactory指定配置文件位置
public class FileProperties {

    private static final String[] DEFAULT_EMPTY_ARRAY = new String[0];

    private String storeDir = "/assets";

    private String singleLimit = "2MB";

    private Integer nums = 10;

    private String domain;

    private String[] exclude = DEFAULT_EMPTY_ARRAY;

    private String[] include = DEFAULT_EMPTY_ARRAY;

    private String servePath = "assets/**";


    public void setStoreDir(String storeDir) {
        this.storeDir = storeDir;
    }

    public void setSingleLimit(String singleLimit) {
        this.singleLimit = singleLimit;
    }

    public void setNums(Integer nums) {
        this.nums = nums;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setExclude(String[] exclude) {
        this.exclude = exclude;
    }

    public void setInclude(String[] include) {
        this.include = include;
    }

    public void setServePath(String servePath) {
        this.servePath = servePath;
    }

    public String getStoreDir() {
        return storeDir;
    }

    public String getServePath() {
        return servePath;
    }

    public String getDomain() {
        return domain;
    }

    public String[] getInclude() {
        return include;
    }

    public String[] getExclude() {
        return exclude;
    }

    public String getSingleLimit() {
        return singleLimit;
    }

    public Integer getNums() {
        return nums;
    }

}
