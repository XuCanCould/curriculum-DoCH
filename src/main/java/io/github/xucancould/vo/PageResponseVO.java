package io.github.xucancould.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * created by Xu on 2024/3/26 9:13.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResponseVO<T> {
    private Integer total;

    private List<T> items;

    private Integer page;

    private Integer count;
}
