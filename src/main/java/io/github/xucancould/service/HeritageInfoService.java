package io.github.xucancould.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.xucancould.dto.heritage.CreateOrUpdateCHIDTO;
import io.github.xucancould.model.HeritageInfoDO;

import java.util.List;

/**
 * created by Xu on 2024/6/14 10:26.
 */
public interface HeritageInfoService extends IService<HeritageInfoDO> {
    boolean createHeritageRecord(CreateOrUpdateCHIDTO bookDO);

    List<HeritageInfoDO> findAll();

    boolean updateCultureHeritageInfo(HeritageInfoDO heritageInfoDO, CreateOrUpdateCHIDTO validator);

    boolean deleteById(Integer id);

    boolean checkHeritageInfoExistById(Integer id);
}
