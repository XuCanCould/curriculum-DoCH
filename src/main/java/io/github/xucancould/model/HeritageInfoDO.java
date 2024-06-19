package io.github.xucancould.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.List;


/**
 * created by Xu on 2024/6/14 10:17.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("heritage_info")
@EqualsAndHashCode(callSuper = true)
public class HeritageInfoDO extends BaseModel implements Serializable {


    // 项目名称
    private String name;

    // 文化遗产类型
    private String heritageType;

    // 所属地区
    private String location;

    // 保护单位
    private String protectionUnit;

    private String summary;

    // 是否数字化
    private Boolean isDigital;

    @TableField(exist = false)
    private List<Integer> archives;
}
