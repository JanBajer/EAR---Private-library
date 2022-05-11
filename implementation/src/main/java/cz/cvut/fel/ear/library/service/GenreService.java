package cz.cvut.fel.ear.library.service;

import cz.cvut.fel.ear.library.dao.GenreDao;
import cz.cvut.fel.ear.library.dao.TitleDao;
import cz.cvut.fel.ear.library.model.Genre;
import cz.cvut.fel.ear.library.model.Title;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class GenreService {
    private final GenreDao dao;
    private final TitleDao titleDao;

    @Autowired
    public GenreService(GenreDao dao, TitleDao titleDao) {
        this.dao = dao;
        this.titleDao = titleDao;
    }

    @Transactional(readOnly = true)
    public List<Genre> findAll() {
        return dao.findAll();
    }

    @Transactional(readOnly = true)
    public Genre findByName(String name) {
        return dao.findByName(name);
    }

    @Transactional
    public void persist(Genre genre) {
        Objects.requireNonNull(genre);
        dao.persist(genre);
    }

    @Transactional
    public void update(Genre genre) {
        Objects.requireNonNull(genre);
        dao.update(genre);
    }

    @Transactional
    public void addTitle(Genre genre, Title title) {
        Objects.requireNonNull(genre);
        Objects.requireNonNull(title);

        title.addGenre(genre);
        titleDao.update(title);
    }

    @Transactional(readOnly = true)
    public Genre find(Integer id) {
        return dao.find(id);
    }

//    @Transactional
//    public void removeGenre(Genre genre, Title title) {
//        Objects.requireNonNull(genre);
//        Objects.requireNonNull(title);
//
////        title.removeGenre(genre);
//        titleDao.update(title);
//    }

}
