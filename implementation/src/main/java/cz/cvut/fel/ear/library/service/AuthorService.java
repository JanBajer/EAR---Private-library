package cz.cvut.fel.ear.library.service;

import cz.cvut.fel.ear.library.dao.AuthorDao;
import cz.cvut.fel.ear.library.dao.TitleDao;
import cz.cvut.fel.ear.library.model.Author;
import cz.cvut.fel.ear.library.model.Title;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AuthorService {
    private final AuthorDao dao;

    private final TitleDao titleDao;

    @Autowired
    public AuthorService(AuthorDao dao, TitleDao titleDao) {
        this.dao = dao;
        this.titleDao = titleDao;
    }

    @Transactional(readOnly = true)
    public List<Author> findAll() {
        return dao.findAll();
    }

    @Transactional(readOnly = true)
    public Author find(Integer id) {
        Objects.requireNonNull(id);

        return dao.find(id);
    }

    @Transactional(readOnly = true)
    public List<Author> findByFullName(String firstName, String lastName) {
        Objects.requireNonNull(firstName);
        Objects.requireNonNull(lastName);

        return dao.findByFullName(firstName, lastName);
    }

    @Transactional(readOnly = true)
    public List<Author> findAllByFirstName(String firstName) {
        Objects.requireNonNull(firstName);

        return dao.findAllByFirstName(firstName);
    }

    @Transactional(readOnly = true)
    public List<Author> findAllByLastName(String lastName) {
        Objects.requireNonNull(lastName);
        return dao.findAllByLastName(lastName);
    }


    @Transactional
    public void persist(Author author) {
        Objects.requireNonNull(author);
        dao.persist(author);
    }

    @Transactional
    public void update(Author author) {
        Objects.requireNonNull(author);
        dao.update(author);
    }


//    @Transactional
//    public void removeAuthor(Author author) {
//        Objects.requireNonNull(author);
//        dao.remove(author);
//    }


    @Transactional //?
    public void addTitle(Author author, Title title) {
        Objects.requireNonNull(author);
        Objects.requireNonNull(title);

        author.addTitle(title);
        titleDao.update(title);
        dao.update(author);
    }

//    @Transactional
//    public void removeTitle(Author author, Title title){
//
//    }

}
