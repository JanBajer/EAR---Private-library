package cz.cvut.fel.ear.library.dao;

import cz.cvut.fel.ear.library.model.Book;
import cz.cvut.fel.ear.library.model.BookLoan;
import cz.cvut.fel.ear.library.model.Reservation;
import cz.cvut.fel.ear.library.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.util.List;

@Repository
public class BookLoanDao extends BaseDao<BookLoan>{
    public BookLoanDao() {
        super(BookLoan.class);
    }

    public List<BookLoan> findByUser(User user){
        try {

            return em.createNamedQuery("Bookloan.findByUser", BookLoan.class)
                    .setParameter("user", user)
                    .getResultList();
        }catch (NoResultException e){
            return null;
        }
    }

    public List<BookLoan> findActiveByDate(LocalDate date){
        try {
            return em.createNamedQuery("Bookloan.findActiveByDate", BookLoan.class)
                    .setParameter("date", date)
                    .getResultList();
        }catch(NoResultException e){
            return null;
        }
    }

    public List<BookLoan> findAllActive(){
        try{
            return em.createNamedQuery("Bookloan.findAllActive", BookLoan.class)
                    .getResultList();
        }catch(NoResultException e){
            return null;
        }
    }

    public List<BookLoan> findAllActiveByBook(Book book){
        try{
            return em.createNamedQuery("Bookloan.findAllActiveByBook", BookLoan.class)
                    .setParameter("book", book)
                    .getResultList();
        }catch(NoResultException e){
            return null;
        }
    }

    public List<BookLoan> findActiveByUser(User user){
        try {

            return em.createNamedQuery("Bookloan.findActiveByUser", BookLoan.class)
                    .setParameter("user", user)
                    .getResultList();
        }catch (NoResultException e){
            return null;
        }
    }
}
