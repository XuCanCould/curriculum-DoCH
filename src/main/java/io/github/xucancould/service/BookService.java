package io.github.xucancould.service;

import io.github.xucancould.dto.book.CreateOrUpdateBookDTO;
import io.github.xucancould.model.BookDO;

import java.util.List;

/**
 * created by Xu on 2024/3/18 9:27.
 */
public interface BookService {
    BookDO getById(Integer id);

    List<BookDO> findAll();

    boolean createBook(CreateOrUpdateBookDTO bookDO);

    List<BookDO> getBookByKeyword(String q);

    boolean updateBook(BookDO book, CreateOrUpdateBookDTO validator);

    boolean deleteById(Integer id);
}
