package io.github.xucancould.service.impl;

import io.github.xucancould.dto.book.CreateOrUpdateBookDTO;
import io.github.xucancould.mapper.BookMapper;
import io.github.xucancould.model.BookDO;
import io.github.xucancould.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * created by Xu on 2024/3/18 9:27.
 */
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookMapper bookMapper;

    @Override
    public BookDO getById(Integer id) {
        return bookMapper.selectById(id);
    }

    @Override
    public List<BookDO> findAll() {
        return bookMapper.selectList(null);
    }

    @Override
    public boolean createBook(CreateOrUpdateBookDTO validator) {
        BookDO bookDO = new BookDO();
        bookDO.setTitle(validator.getTitle());
        bookDO.setAuthor(validator.getAuthor());
        bookDO.setSummary(validator.getSummary());
        bookDO.setImage(validator.getImage());
        return this.bookMapper.insert(bookDO) > 0;
    }

    @Override
    public List<BookDO> getBookByKeyword(String q) {
        return this.bookMapper.selectByTitleLikeKeyword(q);
    }

    @Override
    public boolean updateBook(BookDO book, CreateOrUpdateBookDTO validator) {
        book.setAuthor(validator.getAuthor());
        book.setTitle(validator.getTitle());
        book.setImage(validator.getImage());
        book.setSummary(validator.getSummary());
        return this.bookMapper.updateById(book) > 0;
    }

    @Override
    public boolean deleteById(Integer id) {
        return this.bookMapper.deleteById(id) > 0;
    }
}
