package io.github.xucancould.contoller.v1;

import io.github.talelin.autoconfigure.exception.NotFoundException;
import io.github.talelin.core.annotation.GroupRequired;
import io.github.talelin.core.annotation.PermissionMeta;
import io.github.xucancould.dto.book.CreateOrUpdateBookDTO;
import io.github.xucancould.model.BookDO;
import io.github.xucancould.service.BookService;
import io.github.xucancould.vo.CreatedVo;
import io.github.xucancould.vo.UpdatedVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

/**
 * created by Xu on 2024/3/18 8:58.
 */
@RestController
@RequestMapping("/v1/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/{id}")
    public BookDO getBook(@PathVariable(value = "id") Integer id) {
        BookDO book = bookService.getById(id);
        return book;
    }

    @GetMapping
    public List<BookDO> getBooks() {
        return bookService.findAll();
    }

    @PostMapping
    public CreatedVo createBook(@RequestBody CreateOrUpdateBookDTO book) {
        bookService.createBook(book);
        return new CreatedVo(12);
    }

    @GetMapping("/search")
    public List<BookDO> searchBook(@RequestParam(value = "title", required = false, defaultValue = "") String title) {
        return bookService.getBookByKeyword(title);
    }

    @PutMapping("/{id}")
    public UpdatedVO updateBook(@PathVariable("id") @Positive(message = "{id.positive}") Integer id,
                                @RequestBody @Validated CreateOrUpdateBookDTO validator) {
        BookDO book = bookService.getById(id);
        if (book == null) {
            throw new NotFoundException(10022);
        }
        bookService.updateBook(book, validator);
        return new UpdatedVO(13);
    }

    @DeleteMapping("/{id}")
    @GroupRequired
    @PermissionMeta(value = "删除图书", module = "图书")
    public UpdatedVO deleteBook(@PathVariable("id") @Positive(message = "{id.positive}") Integer id) {
        BookDO book = bookService.getById(id);
        if (book == null) {
            throw new NotFoundException(10022);
        }
        bookService.deleteById(book.getId());
        return new UpdatedVO(14);
    }

}
