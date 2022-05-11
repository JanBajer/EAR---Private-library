package cz.cvut.fel.ear.library.service;

import cz.cvut.fel.ear.library.dao.BookDao;
import cz.cvut.fel.ear.library.model.Book;
import cz.cvut.fel.ear.library.model.Title;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class BookService {
    private final BookDao dao;

    @Autowired
    public BookService(BookDao dao) {
        this.dao = dao;
    }

    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return dao.findAll();
    }

    @Transactional(readOnly = true)
    public Book find(Integer copyId) {
        return dao.find(copyId);
    }

    @Transactional(readOnly = true)
    public List<Book> findAllByTitle(Title title) {
        return dao.findAllByTitle(title);
    }

    @Transactional(readOnly = true)
    public List<Book> findAllByISBN(String ISBN) {
        return dao.findAllByISBN(ISBN);
    }

    @Transactional
    public void persist(Book book) {
        Objects.requireNonNull(book);
        dao.persist(book);
    }

    @Transactional
    public void update(Book book) {
        Objects.requireNonNull(book);
        dao.update(book);
    }



//    @Transactional
//    public void remove(Book book) {
//        Objects.requireNonNull(book);
//        dao.remove(book);
//    }

}
