package io.github.xucancould.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import io.github.xucancould.model.LogDO;

import java.util.Date;

/**
 * created by Xu on 2024/3/23 10:51.
 */
public interface LogService extends IService<LogDO> {

    /**
     * 创建日志
     * @param message   日志消息
     * @param permission 日志涉及的权限
     * @param userId    用户id
     * @param username  用户名
     * @param method    请求方法
     * @param path      请求路径
     * @param status    响应状态
     * @return
     */
    boolean createLog(String message, String permission, Integer userId,
                      String username, String method, String path,
                      Integer status);

    /**
     * 获取用户名分页
     * @param page 当前页
     * @param count 当前页数目
     * @return 用户名分页
     */
    IPage<String> getUserNamePage(Integer page, Integer count);

    /**
     * 搜索日志
     * @param page
     * @param count
     * @param name
     * @param keyWord
     * @param start
     * @param end
     * @return
     */
    IPage<LogDO> searchLogPage(Integer page, Integer count, String name, String keyWord, Date start, Date end);

    /**
     * 分页获取日志
     * @param page 当前页
     * @param count 当前页数目
     * @param name 用户名
     * @param start 日志开启时间
     * @param end 日志结束时间
     * @return 日志数据
     */
    IPage<LogDO> getLogPage(Integer page, Integer count, String name, Date start, Date end);
}
