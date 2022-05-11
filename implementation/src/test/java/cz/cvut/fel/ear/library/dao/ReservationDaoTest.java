package cz.cvut.fel.ear.library.dao;

import cz.cvut.fel.ear.library.Library;
import cz.cvut.fel.ear.library.environment.Generator;
import cz.cvut.fel.ear.library.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ReservationDao Test")
@ExtendWith(SpringExtension.class)
@DataJpaTest
@ComponentScan(basePackageClasses = Library.class)
public class ReservationDaoTest {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private ReservationDao dao;

    @Test
    public void findByUser(){
        Reservation reservation = Generator.generateReservation();
        User user = Generator.generateUser();
        reservation.setUser(user);
//        user.addReservedBook(reservation);

        em.persist(reservation);
        em.persist(user);
        em.persist(reservation.getBook());
        em.persist(reservation.getBook().getTitle());

        List<Reservation> result = dao.findByUser(user);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(reservation, result.get(0));
    }

    @Test
    public void findActiveByDate_retrievesActiveReservationsForSpecificDate(){
        Reservation res1 = Generator.generateReservation();
        Reservation res2 = Generator.generateReservation();
        Reservation res3 = Generator.generateReservation();

        res1.setDateOfReservation(LocalDate.of(2020, 7, 11));
        res2.setDateOfReservation(LocalDate.of(2020, 7, 12));
        res3.setDateOfReservation(LocalDate.of(2020, 5, 13));


        List<Reservation> reservations = new ArrayList<>();
        reservations.add(res1);
        reservations.add(res2);
        reservations.add(res3);
        for (Reservation reservation : reservations){
            reservation.setDateOfExpiration(reservation.getDateOfReservation().plusDays(5));
            em.persist(reservation);
            em.persist(reservation.getUser());
            em.persist(reservation.getBook());
            em.persist(reservation.getBook().getTitle());
        }

        List<Reservation> result = dao.findActiveByDate(LocalDate.of(2020, 7, 12));

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(res1));
        assertTrue(result.contains(res2));
        assertFalse(result.contains(res3));
    }

    @Test
    public void findAllActive_retrievesAllActiveReservations(){

        Reservation res1 = Generator.generateReservation();
        Reservation res2 = Generator.generateReservation();
        Reservation res3 = Generator.generateReservation();
        res1.setActive(true);
        res2.setActive(true);
        res3.setActive(false);
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(res1);
        reservations.add(res2);
        reservations.add(res3);
        for (Reservation res : reservations){
            em.persist(res);
            em.persist(res.getUser());
            em.persist(res.getBook());
            em.persist(res.getBook().getTitle());
        }

        List<Reservation> result = dao.findAllActive();

        assertNotNull(result);
        assertTrue(result.contains(res1));
        assertTrue(result.contains(res2));
        assertFalse(result.contains(res3));
    }

}
