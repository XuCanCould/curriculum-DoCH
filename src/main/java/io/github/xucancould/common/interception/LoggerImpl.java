package io.github.xucancould.common.interception;

import io.github.talelin.autoconfigure.interfaces.LoggerResolver;
import io.github.talelin.core.annotation.Logger;
import io.github.talelin.core.annotation.PermissionMeta;
import io.github.talelin.core.util.BeanUtil;
import io.github.xucancould.service.LogService;
import io.github.xucancould.common.LocalUser;
import io.github.xucancould.model.UserDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * created by Xu on 2024/3/23 10:49.
 */
@Slf4j
@Component
public class LoggerImpl implements LoggerResolver {
    @Autowired
    private LogService logService;


    // 日志格式匹配正则
    private static final Pattern LOG_PATTERN = Pattern.compile("(?<=\\{)[^}]*(?=})");


    @Override
    public void handle(PermissionMeta meta, Logger logger, HttpServletRequest request, HttpServletResponse response) {
        String template = logger.template();
        UserDO localUser = LocalUser.getLocalUser();
        template = parseTemplate(template, localUser, request, response);
        String permission = "";
        if (meta != null) {
            permission = meta.value();
        }
        Integer id = localUser.getId();
        String username = localUser.getUsername();
        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        int status = response.getStatus();
        logService.createLog(template, permission,
                id, username,
                method, requestURI,
                status);
    }

    /**
     * “替换”模板
     * 解析传入的字符串模板，提取标识符对应的属性值
     * @param template
     * @param user
     * @param request
     * @param response
     * @return
     */
    private String parseTemplate(String template, UserDO user, HttpServletRequest request, HttpServletResponse response) {
        Matcher matcher = LOG_PATTERN.matcher(template);
        if (matcher.find()) {
            String group = matcher.group();
            String property = this.extractProperty(group, user, request, response);
            template = template.replace("{" + group + "}", property);
        }
        return template;
    }

    /**
     * 根据传入item的对象名和属性名提取对应值，
     * @param item
     * @param user
     * @param request
     * @param response
     * @return
     */
    private String extractProperty(String item, UserDO user, HttpServletRequest request, HttpServletResponse response) {
        int i = item.lastIndexOf('.');
        String obj = item.substring(0, i);
        String prop = item.substring(i + 1);
        switch (obj) {
            case "user":
                if (user == null) {
                    return "";
                }
                return BeanUtil.getValueByPropName(user, prop);
            case "request":
                return BeanUtil.getValueByPropName(request, prop);
            case "response":
                return BeanUtil.getValueByPropName(response, prop);
            default:
                return "";
        }
    }
}
