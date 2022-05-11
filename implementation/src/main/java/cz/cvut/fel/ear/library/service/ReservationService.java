package cz.cvut.fel.ear.library.service;

import cz.cvut.fel.ear.library.dao.BookDao;
import cz.cvut.fel.ear.library.dao.BookLoanDao;
import cz.cvut.fel.ear.library.dao.ReservationDao;
import cz.cvut.fel.ear.library.dao.UserDao;
import cz.cvut.fel.ear.library.exception.BookNotAvailable;
import cz.cvut.fel.ear.library.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class ReservationService {

    private final ReservationDao dao;
    private final BookDao bookDao;
    private final UserDao userDao;
    private final BookLoanDao bookLoanDao;

    @Autowired
    public ReservationService(ReservationDao dao, BookDao bookDao, UserDao userDao, BookLoanDao bookLoanDao) {
        this.dao = dao;
        this.bookDao = bookDao;
        this.userDao = userDao;
        this.bookLoanDao = bookLoanDao;
    }

    @Transactional
    public Reservation create(Book book, User user){
        Objects.requireNonNull(book);
        Objects.requireNonNull(user);

        if (isReservationAvailable(book)) {

            final Reservation reservation = new Reservation(book, user);

            book.addReservation(reservation);
            book.setBookState(BookStateType.RESERVED);

//            user.addReservedBook(reservation);

            bookDao.update(book);
            userDao.update(user);
            dao.persist(reservation);
            return reservation;
        }else{
            throw new BookNotAvailable("Book is not available for reservation");
        }
    }

    @Transactional
    public void cancel(Reservation reservation){
        Objects.requireNonNull(reservation);
        User user = reservation.getUser();
        Book book = reservation.getBook();
        reservation.setActive(false);
        reservation.setDateOfExpiration(LocalDate.now());

//        user.removeReservedBook(reservation);
        userDao.update(user);

        book.setBookState(BookStateType.AVAILABLE);
        bookDao.update(book);

        dao.update(reservation);
    }

    @Transactional
    public boolean checkIfShouldBeActive(Reservation reservation){
        Objects.requireNonNull(reservation);

        return LocalDate.now().isAfter(reservation.getDateOfExpiration());
    }

    @Transactional
    public void changeReservationState(Reservation reservation){
        Objects.requireNonNull(reservation);

        reservation.setActive(false);

        Book book = reservation.getBook();
        book.setBookState(BookStateType.AVAILABLE);
        bookDao.update(book);

        dao.remove(reservation);
    }


    @Transactional(readOnly = true)
    public boolean isReservationAvailable(Book book){
        Objects.requireNonNull(book);

        List<Reservation> reservations = dao.findAllActiveByBook(book);
        List<BookLoan> bookLoans = bookLoanDao.findAllActiveByBook(book);
        return reservations.isEmpty() && bookLoans.isEmpty();
    }

    @Transactional(readOnly = true)
    public Reservation find(Integer id){
        return dao.find(id);
    }

    @Transactional(readOnly = true)
    public List<Reservation> findActiveByUser(User user) {
        Objects.requireNonNull(user);
        return dao.findActiveByUser(user);
    }


}
