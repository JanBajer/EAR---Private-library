package cz.cvut.fel.ear.library.dao;

import cz.cvut.fel.ear.library.Library;
import cz.cvut.fel.ear.library.environment.Generator;
import cz.cvut.fel.ear.library.model.Author;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("AuthorDao Test")
@ExtendWith(SpringExtension.class)
@DataJpaTest
@ComponentScan(basePackageClasses = Library.class)
public class AuthorDaoTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private AuthorDao dao;

    @DisplayName("Find Should Find Existing Author by ID")
    @Test
    public void findExistingAuthorByID() {
        final Author author = Generator.generateAuthor();
        em.persist(author);

        //author exists
        final Author result = dao.find(author.getId());

        //doesnt exist
        final Author result1 = dao.find(Generator.generateAuthor().getId());

        assertEquals(author, result);
        assertNull(result1);
    }


    @DisplayName("Find By Full Name should find all authors with specific first and last name")
    @Test
    public void findByFullName() {
        List<Author> expected = new ArrayList<>();

        //3 authors with name Jan Bajer
        for (int i = 0; i < 3; ++i) {
            Author author = Generator.generateAuthor();
            author.setFirstName("Jan");
            author.setLastName("Bajer");
            em.persist(author);
            expected.add(author);
        }
        //2 authors with name Jan Truong
        for (int i = 0; i < 2; ++i) {
            Author author = Generator.generateAuthor();
            author.setFirstName("Jan");
            author.setLastName("Truong");
            em.persist(author);
            expected.add(author);
        }
        //2 authors with name Pavel Bajer
        for (int i = 0; i < 2; ++i) {
            Author author = Generator.generateAuthor();
            author.setFirstName("Pavel");
            author.setLastName("Bajer");
            em.persist(author);
            expected.add(author);
        }
        //find authors with name Jan Bajer
        final List<Author> result = dao.findByFullName("Jan","Bajer");
        Assertions.assertEquals(result.size(), 3);

        //find authors with name Pavel Truong
        final List<Author> result1 = dao.findByFullName("Pavel","Bajer");
        Assertions.assertEquals(result1.size(), 2);

        //find authors with name Adam Bajer
        final List<Author> result2 = dao.findByFullName("Adam","Bajer");
        Assertions.assertEquals(result2.size(), 0);
    }

    @DisplayName("Find By First Name should find all authors with specific first name")
    @Test
    public void findAllByFirstName() {
        List<Author> expected = new ArrayList<>();

        //3 authors with name Jan Bajer
        for (int i = 0; i < 3; ++i) {
            Author author = Generator.generateAuthor();
            author.setFirstName("Jan");
            author.setLastName("Bajer");
            em.persist(author);
            expected.add(author);
        }
        //2 authors with name Jan Truong
        for (int i = 0; i < 2; ++i) {
            Author author = Generator.generateAuthor();
            author.setFirstName("Jan");
            author.setLastName("Truong");
            em.persist(author);
            expected.add(author);
        }
        //2 authors with name Pavel Bajer
        for (int i = 0; i < 2; ++i) {
            Author author = Generator.generateAuthor();
            author.setFirstName("Pavel");
            author.setLastName("Bajer");
            em.persist(author);
            expected.add(author);
        }
        //find authors with name Jan
        final List<Author> result = dao.findAllByFirstName("Jan");
        Assertions.assertEquals(result.size(), 5);

        //find authors with name Pavel
        final List<Author> result1 = dao.findAllByFirstName("Pavel");
        Assertions.assertEquals(result1.size(), 2);

        //find authors with name David
        final List<Author> result2 = dao.findAllByFirstName("David");
        Assertions.assertEquals(result2.size(), 0);
    }

    @DisplayName("Find By Last Name should find all authors with specific last name")
    @Test
    public void findAllByLastName() {
        List<Author> expected = new ArrayList<>();

        //3 authors with name Jan Bajer
        for (int i = 0; i < 3; ++i) {
            Author author = Generator.generateAuthor();
            author.setFirstName("Jan");
            author.setLastName("Bajer");
            em.persist(author);
            expected.add(author);
        }
        //2 authors with name Jan Truong
        for (int i = 0; i < 2; ++i) {
            Author author = Generator.generateAuthor();
            author.setFirstName("Jan");
            author.setLastName("Truong");
            em.persist(author);
            expected.add(author);
        }
        //2 authors with name Pavel Bajer
        for (int i = 0; i < 2; ++i) {
            Author author = Generator.generateAuthor();
            author.setFirstName("Pavel");
            author.setLastName("Bajer");
            em.persist(author);
            expected.add(author);
        }
        //find authors with lastname Bajer
        final List<Author> result = dao.findAllByLastName("Bajer");
        Assertions.assertEquals(result.size(), 5);

        //find authors with lastname Truong
        final List<Author> result1 = dao.findAllByLastName("Truong");
        Assertions.assertEquals(result1.size(), 2);

        //find authors with lastname Umlasek
        final List<Author> result2 = dao.findAllByFirstName("Umlasek");
        Assertions.assertEquals(result2.size(), 0);
    }



}

