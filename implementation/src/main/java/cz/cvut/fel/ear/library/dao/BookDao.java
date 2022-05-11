package cz.cvut.fel.ear.library.dao;

import cz.cvut.fel.ear.library.model.Book;
import cz.cvut.fel.ear.library.model.Title;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Objects;

@Repository
public class BookDao extends BaseDao<Book>{

    public BookDao() {
        super(Book.class);
    }

    public Book find(Integer copyId){
        try {
            return em.createNamedQuery("Book.find", Book.class)
                    .setParameter("copyId", copyId)
                    .getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }

    public List<Book> findAllByISBN(String ISBN){
        try{
            return em.createNamedQuery("Book.findAllByISBN", Book.class)
                    .setParameter("ISBN", ISBN)
                    .getResultList();
        }catch (NoResultException e){
            return null;
        }
    }


    public List<Book> findAllByTitle(Title title){
        try{
            return em.createNamedQuery("Book.findAllByTitle", Book.class)
                    .setParameter("title", title)
                    .getResultList();
        }catch (NoResultException e){
            return null;
        }
    }



}
