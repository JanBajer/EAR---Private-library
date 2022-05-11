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
public class ReservationDao extends BaseDao<Reservation> {

    public ReservationDao() {
        super(Reservation.class);
    }



    public List<Reservation> findByUser(User user){
        try {
            return em.createNamedQuery("Reservation.findByUser", Reservation.class)
                    .setParameter("user", user)
                    .getResultList();
        }catch (NoResultException e){
            return null;
        }
    }

    public List<Reservation> findActiveByDate(LocalDate date){
        try {
            return em.createNamedQuery("Reservation.findActiveByDate", Reservation.class)
                    .setParameter("date", date)
                    .getResultList();
        }catch(NoResultException e){
            return null;
        }
    }

    public List<Reservation> findAllActive(){
        try {
            return em.createNamedQuery("Reservation.findAllActive", Reservation.class)
                    .getResultList();
        }catch(RuntimeException e){
            return null;
        }
    }

    public List<Reservation> findAllActiveByBook(Book book){
        try {
            return em.createNamedQuery("Reservation.findAllActiveByBook", Reservation.class)
                    .setParameter("book", book)
                    .getResultList();
        }catch(RuntimeException e){
            return null;
        }
    }

    public List<Reservation> findActiveByUser(User user){
        try {
            return em.createNamedQuery("Reservation.findActiveByUser", Reservation.class)
                    .setParameter("user", user)
                    .getResultList();
        }catch (NoResultException e){
            return null;
        }
    }
}
