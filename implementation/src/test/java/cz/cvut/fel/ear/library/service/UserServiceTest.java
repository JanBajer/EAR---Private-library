package cz.cvut.fel.ear.library.service;

import cz.cvut.fel.ear.library.dao.UserDao;
import cz.cvut.fel.ear.library.environment.Generator;
import cz.cvut.fel.ear.library.model.Book;
import cz.cvut.fel.ear.library.model.User;
import cz.cvut.fel.ear.library.model.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserService Test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserServiceTest {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserService service;

    @Autowired
    private ReservationService reservationService;

    @Test
    public void persist_userWithoutUserTypeIsGuest(){
        User user = Generator.generateUser();
        user.setUserType(null);

        service.persist(user);

        User result = em.find(User.class, user.getId());
        assertEquals(UserType.GUEST, result.getUserType());
    }

    @Test
    public void exists_checksIfUserWithCertainUsernameExists(){
        User user = Generator.generateUser();
        user.setUsername("Pepa");
        em.persist(user);

        assertTrue(service.exists(user.getUsername()));
        assertFalse(service.exists("Nonexisting username"));
    }

    @Test
    public void userExistsReservations(){
        User user = Generator.generateUser();
        user.setUsername("Pepa");

        em.persist(user);

        assertFalse(service.doesUserHaveReservation(user));

        Book book = Generator.generateBook();
        reservationService.create(book, user);

        assertTrue(service.doesUserHaveReservation(user));
    }

}
