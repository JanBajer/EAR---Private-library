package cz.cvut.fel.ear.library.dao;

import cz.cvut.fel.ear.library.Library;
import cz.cvut.fel.ear.library.environment.Generator;
import cz.cvut.fel.ear.library.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@DisplayName("UserDao Test")
@ExtendWith(SpringExtension.class)
@DataJpaTest
@ComponentScan(basePackageClasses = Library.class)
public class UserDaoTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private UserDao dao;

    @DisplayName("Find Should Retrieve Instance By It's Username")
    @Test
    public void findRetrievesInstanceByItsUserName() {
        final User user1 = Generator.generateUser();
        user1.setUsername("Jenda");
        em.persist(user1);
        final User user2 = Generator.generateUser();
        user2.setUsername("Pavlík");
        em.persist(user2);

        final User result = dao.findByUsername("Jenda");

        Assertions.assertEquals("Jenda", result.getUsername());
        Assertions.assertEquals(user1.getUsername(), result.getUsername());
    }

    @DisplayName("getOnlyGuests and getOnlyLibrarian")
    @Test
    public void getSpecificUser() {

        //Generate guests
        for (int i = 0; i < 20; ++i) {
            final User user = Generator.generateUser();
            em.persist(user);
        }

        //Generate librarians
        for (int i = 0; i < 5; ++i) {
            final User user = Generator.generateUser();
            user.setUserType(UserType.LIBRARIAN);
            em.persist(user);
        }

        final List<User> result1 = dao.getOnlyGuests();
        Assertions.assertEquals(20, result1.size());

        final List<User> result2 = dao.getOnlyLibrarian();
        Assertions.assertEquals(5, result2.size());
    }

}

