package io.github.xucancould.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.xucancould.dto.digital.CreateOrUpdateCHDADTO;
import io.github.xucancould.model.DigitalArchivesDO;

import java.util.List;

/**
 * created by Xu on 2024/6/14 10:23.
 */
public interface DigitalArchivesService extends IService<DigitalArchivesDO> {
    List<DigitalArchivesDO> findAll();

    boolean createDigitalArchive(CreateOrUpdateCHDADTO bookDO);

    boolean updateArchives(DigitalArchivesDO digitalArchivesDO, CreateOrUpdateCHDADTO validator);

    boolean deleteById(Integer id);
}
