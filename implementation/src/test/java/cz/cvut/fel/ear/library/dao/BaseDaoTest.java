package cz.cvut.fel.ear.library.dao;

import cz.cvut.fel.ear.library.Library;
import cz.cvut.fel.ear.library.environment.Generator;
import cz.cvut.fel.ear.library.model.Reservation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BaseDao Test")
@ExtendWith(SpringExtension.class)
@DataJpaTest
@ComponentScan(basePackageClasses = Library.class)
public class BaseDaoTest {
    @Autowired
    private TestEntityManager em;
    @Autowired
    private ReservationDao dao;

    @Test
    public void persist_savesSpecifiedInstance(){
        final Reservation reservation = Generator.generateReservation();
        dao.persist(reservation);
        assertNotNull(reservation.getId());

        final Reservation result = em.find(Reservation.class, reservation.getId());

        assertNotNull(result);
        assertEquals(reservation.getId(), result.getId());
        assertEquals(reservation.getBook(), result.getBook());
        assertEquals(reservation.getUser(), result.getUser());
        assertTrue(result.isActive());
        assertEquals(reservation.getDateOfReservation(), result.getDateOfReservation());
        assertEquals(reservation.getDateOfExpiration(), result.getDateOfExpiration());
    }

    @Test
    public void find_findsSpecifiedInstance(){
        final Reservation reservation = Generator.generateReservation();
        em.persist(reservation);

        assertNotNull(reservation.getId());

        final Reservation result = dao.find(reservation.getId());

        assertNotNull(result);
        assertEquals(reservation.getId(), result.getId());
        assertEquals(reservation.getDateOfReservation(), result.getDateOfReservation());
        assertEquals(reservation.getDateOfExpiration(), result.getDateOfExpiration());
    }


    @Test
    public void update_updatesExistingInstance(){
        final Reservation reservation = Generator.generateReservation();
        em.persist(reservation);

        final Reservation update = new Reservation();
        update.setId(reservation.getId());
        update.setActive(false); // update
        dao.update(update);

        final Reservation result = dao.find(reservation.getId());
        assertNotNull(result);
        assertFalse(result.isActive());
    }

    @Test
    public void remove_removesExistingInstance(){
        final Reservation reservation = Generator.generateReservation();
        em.persist(reservation);

        assertNotNull(em.find(Reservation.class, reservation.getId()));
        em.detach(reservation);

        dao.remove(reservation);
        assertNull(em.find(Reservation.class, reservation.getId()));
    }

    @Test
    public void remove_nothingHappensWhenRemovingNonExistingInstance(){
        final Reservation reservation = Generator.generateReservation();
        reservation.setId(123);
        assertNull(em.find(Reservation.class, reservation.getId()));

        dao.remove(reservation);
        assertNull(em.find(Reservation.class, reservation.getId()));
    }

    @Test
    public void exists_returnsTrueForExistingInstance(){
        final Reservation reservation = Generator.generateReservation();
        reservation.setId(123);
        em.persist(reservation);

        assertTrue(dao.exists(reservation.getId()));
    }

    public void exists_returnsFalseForNonExistingInstance(){
        final Reservation reservation = Generator.generateReservation();
        reservation.setId(123);
        em.persist(reservation);

        assertFalse(dao.exists(-1));
    }
}
