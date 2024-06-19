package io.github.xucancould.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.xucancould.common.enumeration.CulturalHeritageTypeEnum;
import io.github.xucancould.dto.heritage.CreateOrUpdateCHIDTO;
import io.github.xucancould.mapper.HeritageInfoMapper;
import io.github.xucancould.model.HeritageInfoDO;
import io.github.xucancould.service.HeritageInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * created by Xu on 2024/6/14 10:27.
 */
@Service
public class HeritageInfoServiceImpl extends ServiceImpl<HeritageInfoMapper, HeritageInfoDO> implements HeritageInfoService {
    @Override
    public boolean createHeritageRecord(CreateOrUpdateCHIDTO heritageInfoDO) {
        HeritageInfoDO heritageInfo = new HeritageInfoDO();
        setHeritageInfoDO(heritageInfo, heritageInfoDO);
        return this.baseMapper.insert(heritageInfo) > 0;
    }

    @Override
    public List<HeritageInfoDO> findAll() {
        return this.baseMapper.selectList(null);
    }

    @Override
    public boolean updateCultureHeritageInfo(HeritageInfoDO heritageInfo, CreateOrUpdateCHIDTO validator) {
        setHeritageInfoDO(heritageInfo, validator);
        return this.baseMapper.updateById(heritageInfo) > 0;
    }

    @Override
    public boolean deleteById(Integer id) {
        return this.baseMapper.deleteById(id) > 0;
    }

    private HeritageInfoDO setHeritageInfoDO(HeritageInfoDO heritageInfo, CreateOrUpdateCHIDTO validator) {
        heritageInfo.setName(validator.getName());
        String cultureHeritageType = CulturalHeritageTypeEnum.getDescriptionByValue(validator.getHeritageType());
        heritageInfo.setHeritageType(cultureHeritageType);
        heritageInfo.setIsDigital(validator.isDigital());
        heritageInfo.setLocation(validator.getLocation());
        heritageInfo.setProtectionUnit(validator.getProtectionUnit());
        heritageInfo.setSummary(validator.getSummary());
        return heritageInfo;
    }


}
