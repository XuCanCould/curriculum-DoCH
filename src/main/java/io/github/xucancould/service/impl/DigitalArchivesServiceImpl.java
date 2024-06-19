package io.github.xucancould.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.xucancould.common.enumeration.DigitalModeEnum;
import io.github.xucancould.dto.digital.CreateOrUpdateCHDADTO;
import io.github.xucancould.mapper.DigitalArchivesMapper;
import io.github.xucancould.model.DigitalArchivesDO;
import io.github.xucancould.service.DigitalArchivesService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * created by Xu on 2024/6/14 10:24.
 */
@Service
public class DigitalArchivesServiceImpl extends ServiceImpl<DigitalArchivesMapper, DigitalArchivesDO> implements DigitalArchivesService {
    @Override
    public List<DigitalArchivesDO> findAll() {
        return this.baseMapper.selectList(null);
    }

    @Override
    public boolean createDigitalArchive(CreateOrUpdateCHDADTO chdadto) {
        DigitalArchivesDO archivesDO = new DigitalArchivesDO();
        archivesDO = setDigitalArchivesDO(archivesDO, chdadto);
        return this.baseMapper.insert(archivesDO) > 0;
    }

    @Override
    public boolean updateArchives(DigitalArchivesDO digitalArchivesDO, CreateOrUpdateCHDADTO validator) {
        digitalArchivesDO = setDigitalArchivesDO(digitalArchivesDO, validator);
        return this.baseMapper.updateById(digitalArchivesDO) > 0;
    }

    @Override
    public boolean deleteById(Integer id) {
        return this.baseMapper.deleteById(id) > 0;
    }

    private DigitalArchivesDO setDigitalArchivesDO(DigitalArchivesDO digitalArchivesDO, CreateOrUpdateCHDADTO validator) {
        digitalArchivesDO.setHeritageId(validator.getCultureHeritageId());
        digitalArchivesDO.setArchivePath(validator.getArchivePath());
        digitalArchivesDO.setArchiveType(DigitalModeEnum.getDescriptionByValue(validator.getArchiveType()));
        // todo 图片字段赋值
        return digitalArchivesDO;
    }

}
