package io.github.xucancould.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;

/**
 * created by Xu on 2024/3/23 11:39.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("lin_log")
@EqualsAndHashCode(callSuper = true)
public class LogDO extends BaseModel implements Serializable {

    private static final long serialVersionUID = -7471474245124682011L;

    private String message;

    private Integer userId;

    private String username;

    private Integer statusCode;

    private String method;

    private String path;

    private String permission;

}
