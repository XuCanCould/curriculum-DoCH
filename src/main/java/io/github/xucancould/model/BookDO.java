package io.github.xucancould.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 * 图书数据对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("book")
@EqualsAndHashCode(callSuper = true)
// 对象比较时，默认不比较父类属性的值，而设置为true，父类字段会被进行比较
public class BookDO extends BaseModel implements Serializable {

    private static final long serialVersionUID = 3531805912578317266L;

    private String title;

    private String author;

    private String summary;

    private String image;
}
