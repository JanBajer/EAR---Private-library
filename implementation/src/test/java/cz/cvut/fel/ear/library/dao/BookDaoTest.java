package cz.cvut.fel.ear.library.dao;

import cz.cvut.fel.ear.library.Library;
import cz.cvut.fel.ear.library.environment.Generator;
import cz.cvut.fel.ear.library.model.Author;
import cz.cvut.fel.ear.library.model.Book;
import cz.cvut.fel.ear.library.model.Genre;
import cz.cvut.fel.ear.library.model.Title;
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

@DisplayName("BookDao Test")
@ExtendWith(SpringExtension.class)
@DataJpaTest
@ComponentScan(basePackageClasses = Library.class)
public class BookDaoTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private BookDao dao;

    @DisplayName("Find Should Retrieve Instance By It's Identifier(copyId)")
    @Test
    public void findRetrievesInstanceByItsIdentifier() {
        final Title title = Generator.generateTitle();
        em.persist(title);
        final Book book = Generator.generateBook();
        book.setTitle(title);
        em.persist(book);

        final Book result = dao.find(book.getBookID());

        Assertions.assertEquals(book.getBookID(), result.getBookID());

    }

    @DisplayName("Find all by ISBN Should Retrieve Instances with specific ISBN")
    @Test
    public void findAllByISBNRetrievesInstancesByISBN() {
        final Title title = Generator.generateTitle();
        em.persist(title);
        final List<Book> expected = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            final Book book = Generator.generateBook();
            book.setTitle(title);
            expected.add(book);
            em.persist(book);
        }

        final List<Book> result = dao.findAllByISBN(title.getISBN());
        expected.sort(Comparator.comparing(Book::getBookID));

        Assertions.assertEquals(expected, result);
        Assertions.assertEquals(expected.size(), result.size());
        Assertions.assertEquals(5, result.size());

    }

    @DisplayName("Find all by Title Retrieves All Instances With Specified Title")
    @Test
    public void findAllByTitleRetrievesAllInstancesWithSpecificTitle() {
        final Title title1 = Generator.generateTitle();
        final Title title2 = Generator.generateTitle();
        em.persist(title1);
        em.persist(title2);
        final List<Book> expected1 = new ArrayList<>();
        final List<Book> expected2 = new ArrayList<>();

        for (int i = 0; i < 5; ++i) {
            final Book book = Generator.generateBook();
            book.setTitle(title1);
            expected1.add(book);
            em.persist(book);
        }
        for (int i = 0; i < 7; ++i) {
            final Book book = Generator.generateBook();
            book.setTitle(title2);
            expected2.add(book);
            em.persist(book);
        }

        final List<Book> result1 = dao.findAllByTitle(title1);
        final List<Book> result2 = dao.findAllByTitle(title2);

        Assertions.assertNotNull(result1);
        Assertions.assertEquals(expected1.size(), result1.size());
        Assertions.assertEquals(5, result1.size());

        Assertions.assertNotNull(result2);
        Assertions.assertEquals(expected2.size(), result2.size());
        Assertions.assertEquals(7, result2.size());
    }



}

