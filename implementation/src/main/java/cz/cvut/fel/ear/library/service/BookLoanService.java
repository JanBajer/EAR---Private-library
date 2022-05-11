package cz.cvut.fel.ear.library.service;

import cz.cvut.fel.ear.library.dao.BookDao;
import cz.cvut.fel.ear.library.dao.BookLoanDao;
import cz.cvut.fel.ear.library.dao.ReservationDao;
import cz.cvut.fel.ear.library.dao.UserDao;
import cz.cvut.fel.ear.library.exception.BookNotAvailable;
import cz.cvut.fel.ear.library.exception.UserTypeException;
import cz.cvut.fel.ear.library.model.*;
import cz.cvut.fel.ear.library.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class BookLoanService {

    private final BookLoanDao dao;
    private final BookDao bookDao;
    private final UserDao userDao;
    private final ReservationDao reservationDao;

    @Autowired
    public BookLoanService(BookLoanDao dao, BookDao bookDao, UserDao userDao, ReservationDao reservationDao) {
        this.dao = dao;
        this.bookDao = bookDao;
        this.userDao = userDao;
        this.reservationDao = reservationDao;
    }

    @Transactional
    public BookLoan lendBook(Book book, User user){
        Objects.requireNonNull(book);
        Objects.requireNonNull(user);


        if (userHasReservations(book,user) || isAvailable(book)){
            if (user.getUserType() == UserType.GUEST) {

                final BookLoan bookloan = new BookLoan(book, user);
                bookloan.setDateOfLoan(LocalDate.now());
                bookloan.setDateOfSupposedReturn(LocalDate.now().plusDays(Constants.DAYS_OF_BOOKLOAN)); // bookloan for a month (30 days)

                book.setBookState(BookStateType.LEND);
                book.addBookloan(bookloan);

//            user.addBookLoan(bookloan);

                bookDao.update(book);
                userDao.update(user);
                dao.persist(bookloan);

                return bookloan;
            }else throw new UserTypeException("User not guest!");
        }else{
            throw new BookNotAvailable("Book not available for book loan!");
        }
    }

    @Transactional
    public void returnBook(BookLoan bookLoan){
        Objects.requireNonNull(bookLoan);
        User user = bookLoan.getUser();
        Book book = bookLoan.getBook();
        bookLoan.setActive(false);
        bookLoan.setDateOfReturn(LocalDate.now());

//        user.removeBookloan(bookLoan);
        userDao.update(user);

        book.setBookState(BookStateType.AVAILABLE);
        bookDao.update(book);

        dao.update(bookLoan);

    }

    @Transactional(readOnly = true)
    public boolean userHasReservations(Book book, User user){
        Objects.requireNonNull(book);
        Objects.requireNonNull(user);

        List<Reservation> reservations = reservationDao.findAllActiveByBook(book);
        boolean hasReservation = false;
        for (Reservation r : reservations){
            if (r.getUser().equals(user)){
                hasReservation = true;

                r.setActive(false);
                r.setDateOfExpiration(LocalDate.now());

                userDao.update(r.getUser());

                book.setBookState(BookStateType.AVAILABLE);
                bookDao.update(r.getBook());

                reservationDao.update(r);

                break;
            }
        }

        return hasReservation;
    }

    @Transactional(readOnly = true)
    public boolean isAvailable(Book book){
        Objects.requireNonNull(book);

        List<BookLoan> bookLoans = dao.findAllActiveByBook(book);
        List<Reservation> reservations = reservationDao.findAllActiveByBook(book);

        return bookLoans.isEmpty() && reservations.isEmpty();
    }

    @Transactional(readOnly = true)
    public BookLoan find(Integer id){
        return dao.find(id);
    }
}
