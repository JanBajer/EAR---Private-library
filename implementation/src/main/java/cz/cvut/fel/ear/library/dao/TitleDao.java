package cz.cvut.fel.ear.library.dao;

import cz.cvut.fel.ear.library.model.Author;
import cz.cvut.fel.ear.library.model.Book;
import cz.cvut.fel.ear.library.model.Genre;
import cz.cvut.fel.ear.library.model.Title;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.OrderBy;
import java.util.List;
import java.util.Objects;

@Repository
public class TitleDao extends BaseDao<Title> {
    public TitleDao() {
        super(Title.class);
    }

    public List<Title> findByGenre(Genre genre){
        try{
            return em.createNamedQuery("Title.findByGenre", Title.class)
                    .setParameter("genre", genre)
                    .getResultList();
        }catch (NoResultException e){
            return null;
        }
    }

    public List<Title> findByAuthor(Author author){
        try{
            return em.createNamedQuery("Title.findByAuthor", Title.class)
                    .setParameter("author", author)
                    .getResultList();
        }catch (NoResultException e){
            return null;
        }
    }

    public Title findByName(String name, String isbn){
        try{
            return em.createNamedQuery("Title.findByNameAndISBN", Title.class)
                    .setParameter("name", name)
                    .setParameter("ISBN", isbn)
                    .getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

}
