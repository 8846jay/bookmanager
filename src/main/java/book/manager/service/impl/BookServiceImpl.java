package book.manager.service.impl;

import book.manager.entity.Book;
import book.manager.entity.Borrow;
import book.manager.entity.BorrowDetails;
import book.manager.mapper.BookMapper;
import book.manager.mapper.UserMapper;
import book.manager.service.BookService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    @Resource
    BookMapper mapper;

    @Resource
    UserMapper userMapper;

    @Override
    public List<Book> getAllBook() {
        return mapper.allBook();
    }

    public List<Book> getAllBookWithOutBorrow() {
        List<Book> books = mapper.allBook();
        List<Integer> borrows = mapper.borrowList()
                .stream()
                .map(Borrow::getBid)
                .collect(Collectors.toList());

        return books
                .stream()
                .filter(book -> !borrows.contains(book.getBid()))
                .collect(Collectors.toList());

    }

    @Override
    public List<Book> getAllBorrowedBookById(int id) {
        Integer sid = userMapper.getSidByUserId(id);
        if(sid == null) return Collections.emptyList();
        return mapper.borrowListBySid(sid)
                .stream()
                .map(borrow -> mapper.getBookById(borrow.getBid()))
                .collect(Collectors.toList());
    }

    @Override
    public List<BorrowDetails> getAllBorrowDetails() {
        return mapper.borrowDetailsList();
    }


    @Override
    public void deleteBook(int bid) {
        mapper.deleteBook(bid);
    }

    @Override
    public void addBook(String title,String desc, Double price) {
        mapper.addBook(title, desc, price);
    }

    @Override
    public void borrowBook(int bid,int id) {
        Integer sid = userMapper.getSidByUserId(id);
        if(sid == null) return;
        mapper.addBorrow(bid, sid);

    }

    @Override
    public void returnBook(int bid, int id) {
        Integer sid = userMapper.getSidByUserId(id);
        if(sid == null) return;
        mapper.deleteBorrow(bid, sid);
    }
}
