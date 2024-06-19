package io.github.xucancould.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.util.Arrays;
import java.util.Objects;

/**
 * created by Xu on 2024/3/23 21:15.
 */
@Slf4j
public class BeanCopyUtil extends BeanUtils {
    /**
     * 将目标对象的非空属性复制到另一个对象中
     * 1、通过反射获取所有get方法，并获取属性的值
     * 2、将属性值复制到另一个对象中
     * @param source
     * @param target
     */
    // TODO 待优化
    public static void copyNonNullProperties(Object source, Object target) {
        String[] properties = Arrays.stream(ReflectionUtils.getDeclaredMethods(source.getClass()))
                .map(method -> {
                    if (method.getName().startsWith("get")) {
                        Object fieldValue = null;
                        try {
                            fieldValue = method.invoke(source);
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                        if (fieldValue == null) {
                            String fieldName = method.getName().substring(3);
                            return com.baomidou.mybatisplus.core.toolkit.StringUtils.firstToLowerCase(fieldName);
                        }
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .toArray(String[]::new);
        copyProperties(source, target, properties);
    }


}
