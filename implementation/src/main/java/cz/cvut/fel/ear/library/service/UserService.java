package cz.cvut.fel.ear.library.service;

import cz.cvut.fel.ear.library.dao.BookLoanDao;
import cz.cvut.fel.ear.library.dao.ReservationDao;
import cz.cvut.fel.ear.library.dao.UserDao;
import cz.cvut.fel.ear.library.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {
    private final UserDao dao;
    private final ReservationDao reservationDao;
    private final BookLoanDao bookLoanDao;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserService(UserDao dao, ReservationDao reservationDao, BookLoanDao bookLoanDao, PasswordEncoder passwordEncoder) {
        this.dao = dao;
        this.reservationDao = reservationDao;
        this.bookLoanDao = bookLoanDao;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean doesUserHaveReservation(User user){
        Objects.requireNonNull(user);

        return !reservationDao.findActiveByUser(user).isEmpty();
    }

    public boolean doesUserHaveBookLoans(User user){
        Objects.requireNonNull(user);

        return !bookLoanDao.findActiveByUser(user).isEmpty();
    }

    @Transactional
    public void persist(User user){
        Objects.requireNonNull(user);
        user.encodePassword(passwordEncoder);
        if (user.getUserType() == null){
            user.setUserType(UserType.GUEST);
        }
        dao.persist(user);
    }

    @Transactional(readOnly = true)
    public boolean exists(String username) {
        return dao.findByUsername(username) != null;
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return dao.findAll();
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        Objects.requireNonNull(username);
        return dao.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public User find(Integer id) {
        Objects.requireNonNull(id);
        return dao.find(id);
    }

    @Transactional(readOnly = true)
    public List<User> findByPartOfUsername(String username) {
        Objects.requireNonNull(username);
        return dao.findByPartOfUsername(username);
    }

    @Transactional
    public void update(User user) {
        Objects.requireNonNull(user);
        user.encodePassword(passwordEncoder);
        dao.update(user);
    }

}
