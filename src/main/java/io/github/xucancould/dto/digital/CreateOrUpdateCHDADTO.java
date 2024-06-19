package io.github.xucancould.dto.digital;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * created by Xu on 2024/3/25 10:08.
 * culture heritage digital archive
 */
@Data
@NoArgsConstructor
public class CreateOrUpdateCHDADTO {


    @NotEmpty(message = "${heritage.cultureHeritageId.not-empty}")
    private Integer cultureHeritageId;

    @NotEmpty(message = "${digitalArchive.name.not-empty}")
    @Length(max = 50, message = "${digitalArchive.name.length}")
    private String name;

    @NotEmpty(message = "${digitalArchiveType.not-empty}")
    private Integer archiveType;

    @NotEmpty(message = "${heritage.description.not-empty}")
    @Length(max = 800, message = "${heritage.description.length}")
    private String archiveName;
    
    @NotEmpty(message = "${heritage.location.not-empty}")
    @Length(max = 120, message = "${heritage.location.length}")
    private String archivePath;
}
