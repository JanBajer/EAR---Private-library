package cz.cvut.fel.ear.library.service;

import cz.cvut.fel.ear.library.environment.Generator;
import cz.cvut.fel.ear.library.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ReservationService Test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class ReservationServiceTest {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ReservationService service;

    @Test
    public void create_reservedBookChangesBookState(){
        Book book = Generator.generateBook();
        User user = Generator.generateUser();
        book.setBookState(BookStateType.AVAILABLE);
        book.setBookID(1);

        Title t = Generator.generateTitle();
        t.addBookCopy(book);
        book.setTitle(t);
//        book.setReservedBook(null);
        em.persist(book);
        em.persist(user);

        Reservation result = service.create(book, user);

        assertEquals(book, result.getBook());
        assertEquals(BookStateType.RESERVED, result.getBook().getBookState());
        assertEquals(user, result.getUser());
        assertNotNull(result.getBook().getReservedBook());

    }

    @Test
    public void cancel_removesReservation(){
        Reservation reservation = Generator.generateReservation();
        User user = reservation.getUser();
        Book book = reservation.getBook();

        em.persist(reservation);

        service.cancel(reservation);

        assertFalse(em.find(Reservation.class, reservation.getId()).isActive());
        assertFalse(book.getReservedBook().get(0).isActive());
//        assertFalse(user.getListOfReservedBooks().contains(reservation));
    }

}
