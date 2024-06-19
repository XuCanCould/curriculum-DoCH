package io.github.xucancould.common.listener;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.talelin.autoconfigure.bean.MetaInfo;
import io.github.talelin.autoconfigure.bean.PermissionMetaCollector;
import io.github.xucancould.service.PermissionService;
import io.github.xucancould.model.PermissionDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.List;
import java.util.Map;

/**
 * created by Xu on 2024/3/19 13:19.
 * 在 spring 初始化后自动同步数据库中的权限(添加新权限+移除未使用权限)
 */
public class PermissionHandleListener implements ApplicationListener<ContextRefreshedEvent> {

    // 获取所有权限元
    @Autowired
    private PermissionMetaCollector metaCollector;

    // 操作权限数据
    @Autowired
    private PermissionService permissionService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        addNewPermission();
        removeUnusedPermission();
    }

    /**
     * 读取 metaCollector 中的权限，添加不存在的权限
     */
    private void addNewPermission() {
        metaCollector.getMetaMap().values().forEach((meta) -> {
            String module = meta.getModule();
            String permission = meta.getPermission();
            createPermissionIfNotExist(permission, module);
        });
    }

    /**
     * 遍历 permissionService 的权限集合（即常用权限），当存在不匹配（即不常用）的权限
     * 关闭该权限
     */
    private void removeUnusedPermission() {
        List<PermissionDO> allPermissions = permissionService.list();
        Map<String, MetaInfo> metaMap = metaCollector.getMetaMap();
        for(PermissionDO permission : allPermissions) {
            boolean stayedInMeta = metaMap.values().stream()
                    .anyMatch(meta -> meta.getModule().equals(permission.getModule())
                    && meta.getPermission().equals(permission.getName()));
            if (!stayedInMeta) {
                permission.setMount(false);
                permissionService.updateById(permission);
            }
        }
    }

    /**
     * 查询数据库是否存在对应的权限，如果已经存在开启使用(Mount)，不存在在数据库中创建
     * @param name 权限名称，例如：访问首页
     * @param module 权限控制的板块
     */
    private void createPermissionIfNotExist(String name, String module) {
        QueryWrapper<PermissionDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PermissionDO::getModule, module).eq(PermissionDO::getName, name);
        PermissionDO permission = permissionService.getOne(wrapper);
        if (permission == null) {
            permissionService.save(PermissionDO.builder().module(module).name(name).build());
        }
        if (permission != null && !permission.getMount()) {
            permission.setMount(true);
            permissionService.updateById(permission);
        }
    }
}
