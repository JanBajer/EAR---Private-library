package cz.cvut.fel.ear.library.service;

import cz.cvut.fel.ear.library.environment.Generator;
//import cz.cvut.fel.ear.library.exception.InvalidIDException;
import cz.cvut.fel.ear.library.model.Author;
import cz.cvut.fel.ear.library.model.Title;
import cz.cvut.fel.ear.library.service.AuthorService;
import org.junit.jupiter.api.Assertions;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DisplayName("AuthorService Test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class AuthorServiceTest {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private AuthorService service;

    @DisplayName("Add Title Adds Title to Target Author and then Try Remove")
    @Test
    public void addTitleAndRemoveTitle() {

        final Author author = Generator.generateAuthor();

        final List<Title> expected = new ArrayList<>();

        final Title title = Generator.generateTitle();
        title.setAuthor(null);

        final Title title2 = Generator.generateTitle();
        title2.setAuthor(null);

        expected.add(title);
        expected.add(title2);


        em.persist(title);
        em.persist(title2);

        service.persist(author);
        service.addTitle(author, title);
        service.addTitle(author, title2);

        final Author result1 = service.find(author.getId());

        //ADD TEST
        Assertions.assertEquals(2,author.getTitles().size());
        Assertions.assertEquals(expected.size(),author.getTitles().size());
        Assertions.assertEquals(author.getTitles(), result1.getTitles());

        //REMOVE TEST
//        service.removeTitle(author,title);
//        final Author result2 = service.find(author.getId());
//        Assertions.assertEquals(1,author.getTitles().size());
//        Assertions.assertEquals(author.getTitles(), result2.getTitles());


    }

    @DisplayName("Test Persist, Update, Remove")
    @Test
    public void persistShouldPersistValidAuthor() {
        final Author author = Generator.generateAuthor();

        service.persist(author);
        final Author result = service.find(author.getId());
        Assertions.assertEquals(author, result);

//        service.removeAuthor(author);
//        service.update(author);
//        final Author result2 = service.find(author.getId());
//        Assertions.assertEquals(author, result2);
    }

    @DisplayName("Find By Full Name Should Retrieve Authors with specific Full Name")
    @Test
    public void findByFullName() {
        List<Author> expected = new ArrayList<>();

        final Author author1 = Generator.generateAuthor();
        final Author author2 = Generator.generateAuthor();
        final Author author3 = Generator.generateAuthor();
        final Author author4 = Generator.generateAuthor();


        author1.setFirstName("Jan"); author1.setLastName("Bajer");
        author2.setFirstName("Jan"); author2.setLastName("Bajer");
        author3.setFirstName("Jan"); author3.setLastName("Bajer");

        expected.add(author1);
        expected.add(author2);
        expected.add(author3);

        service.persist(author1); //or should be em?
        service.persist(author2);
        service.persist(author3);
        service.persist(author4);

        final List<Author> result = service.findByFullName("Jan", "Bajer");

        Assertions.assertEquals(expected, result);
        Assertions.assertEquals(expected.size(), result.size());
        Assertions.assertEquals(3, result.size());
    }

    @DisplayName("RemoveExistingAuthor")
    @Test
    public void removeAuthor() {
        //PREPARE
        final Author author1 = Generator.generateAuthor();
        service.persist(author1);

        final Author author2 = Generator.generateAuthor();
        service.persist(author2);

        final List<Author> result = service.findAll();

        Assertions.assertEquals(2, result.size());

        //REMOVE
//        service.removeAuthor(author1);
//
//        final List<Author> result2 = service.findAll();
//
//        Assertions.assertEquals(1, result2.size());

    }



}

