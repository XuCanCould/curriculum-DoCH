package io.github.xucancould.dto.query;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * created by Xu on 2024/3/26 9:06.
 * 基础分页数据传输对象
 */
@Data
public class BasePageDTO {
    @Max(value = 30, message = "{page.count.max}")
    @Min(value = 1, message = "{page.count.min}")
    private Integer count;

    @Min(value = 0, message = "{page.number.min}")
    private Integer page;

    public Integer getCount() {
        if (count == null) {
            return 10;
        }
        return count;
    }

    public Integer getPage() {
        if (page == null) {
            return 0;
        }
        return page;
    }
}
