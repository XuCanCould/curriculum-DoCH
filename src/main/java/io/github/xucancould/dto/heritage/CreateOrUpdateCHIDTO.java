package io.github.xucancould.dto.heritage;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * created by Xu on 2024/3/25 10:08.
 * CHI culture heritage info
 */
@Data
@NoArgsConstructor
public class CreateOrUpdateCHIDTO {

    @NotEmpty(message = "${heritage.name.not-empty}")
    @Length(max = 50, message = "${heritage.name.length}")
    private String name;

    @NotNull(message = "${heritageType.not-empty}")
    private Integer heritageType;

    @NotEmpty(message = "${heritage.location.not-empty}")
    @Length(max = 120, message = "${heritage.location.length}")
    private String location;

    @NotEmpty(message = "{heritage.description.not-empty}")
    @Length(max = 800, message = "{heritage.description.length}")
    private String summary;

    @NotEmpty(message = "${heritage.protectionUnit.not-empty}")
    @Length(max = 50, message = "${heritage.protectionUnit.length}")
    private String protectionUnit;

    private boolean isDigital = false;
}
