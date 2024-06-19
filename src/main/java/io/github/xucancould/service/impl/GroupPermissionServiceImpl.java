package io.github.xucancould.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.xucancould.mapper.GroupPermissionMapper;
import io.github.xucancould.model.GroupPermissionDO;
import io.github.xucancould.service.GroupPermissionService;
import org.springframework.stereotype.Service;

/**
 * created by Xu on 2024/3/28 21:38.
 */
@Service
public class GroupPermissionServiceImpl extends ServiceImpl<GroupPermissionMapper, GroupPermissionDO> implements GroupPermissionService {
}
