package io.github.xucancould.dto.book;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * created by Xu on 2024/3/25 10:08.
 */
@Data
@NoArgsConstructor
public class CreateOrUpdateBookDTO {
    @NotEmpty(message = "${book.title.not-empty}")
    @Length(max = 50, message = "${book.title.length}")
    private String title;

    @NotEmpty(message = "${book.author.not-empty}")
    @Length(max = 50, message = "${book.author.length}")
    private String author;

    @NotEmpty(message = "${book.summary.not-empty}")
    @Length(max = 1000, message = "${book.summary.length}")
    private String summary;

    @Length(max = 100, message = "${book.image.length}")
    private String image;
}
