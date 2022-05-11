package cz.cvut.fel.ear.library.service;

import cz.cvut.fel.ear.library.dao.TitleDao;
import cz.cvut.fel.ear.library.model.Author;
import cz.cvut.fel.ear.library.model.Genre;
import cz.cvut.fel.ear.library.model.Title;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class TitleService {
    private final TitleDao dao;

    @Autowired
    public TitleService(TitleDao dao) {
        this.dao = dao;
    }

    @Transactional(readOnly = true)
    public List<Title> findAll() {
        return dao.findAll();
    }

    @Transactional(readOnly = true)
    public List<Title> findByGenre(Genre genre) {
        return dao.findByGenre(genre);
    }

    @Transactional(readOnly = true)
    public List<Title> findByAuthor(Author author) {
        return dao.findByAuthor(author);
    }


    @Transactional(readOnly = true)
    public Title findByName(String name, String isbn) {
        return dao.findByName(name, isbn);
    }

    @Transactional(readOnly = true)
    public Title find(Integer id) {
        return dao.find(id);
    }

    @Transactional
    public void persist(Title title) {
        Objects.requireNonNull(title);
        dao.persist(title);
    }

    @Transactional
    public void update(Title title) {
        Objects.requireNonNull(title);
        dao.update(title);
    }

//    @Transactional
//    public void remove(Title title) {
//        Objects.requireNonNull(title);
//        dao.remove(title);
//    }



}
