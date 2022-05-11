package cz.cvut.fel.ear.library.dao;

import cz.cvut.fel.ear.library.Library;
import cz.cvut.fel.ear.library.environment.Generator;
import cz.cvut.fel.ear.library.model.Author;
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
import java.util.List;

@DisplayName("TitleDao Test")
@ExtendWith(SpringExtension.class)
@DataJpaTest
@ComponentScan(basePackageClasses = Library.class)
public class TitleDaoTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private TitleDao dao;

    @DisplayName("Find Should Retrieve Instance By It's Identifier(ISBN)")
    @Test
    public void findRetrievesInstanceByItsIdentifier() {
        final Title title = Generator.generateTitle();
        em.persist(title);

        final Title result = dao.find(title.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(title.getISBN(), result.getISBN());

    }

    @DisplayName("Find by Genre Should Retrieve Instances With Specific Genre")
    @Test
    public void findByGenreRetrievesInstancesWithSpecificGenre() {
        final Genre genre1 = Generator.generateGenre();
        final Genre genre2 = Generator.generateGenre();
        genre1.setName("horor");
        genre2.setName("komedie");

        em.persist(genre1);
        em.persist(genre2);

        final List<Title> expected = new ArrayList<>();

        for (int i = 0; i < 5; ++i) {
            final Title title = Generator.generateTitle();
            genre1.addTitle(title);
            title.addGenre(genre1);
            expected.add(title);
            em.persist(title);

        }
        for (int i = 0; i < 6; ++i) {
            final Title title = Generator.generateTitle();
            genre2.addTitle(title);
            title.addGenre(genre2);
            em.persist(title);

        }

        final List<Title> result1 = dao.findByGenre(genre1); //horor (5)
        final List<Title> result2 = dao.findByGenre(genre2); //komedie (6)
        final List<Title> result3 = dao.findAll(); //all (11)

        Assertions.assertNotNull(result1); //not null
        Assertions.assertEquals(expected.size(), result1.size()); //horor
        Assertions.assertEquals(6, result2.size()); //komedie
        Assertions.assertEquals(11, result3.size());  //all

    }

    @DisplayName("Find by Author Should Retrieve Instances With Specific Author")
    @Test
    public void findByAuthorRetrievesInstancesWithSpecificAuthor() {

        final List<Title> expected = new ArrayList<>();
        final Author author = Generator.generateAuthor();
        for (int i = 0; i < 5; ++i) {
            final Title title = Generator.generateTitle();

            title.setAuthor(author);
            em.persist(title);
            expected.add(title);
        }
        final Title title1 = Generator.generateTitle();
        final Title title2 = Generator.generateTitle();
        em.persist(title1);
        em.persist(title2);

        final List<Title> result = dao.findByAuthor(author);


        Assertions.assertNotNull(result);
        //there is 7 titles but only 5 of them have author we want
        Assertions.assertEquals(expected.size(), result.size());
    }


    @Test
    public void orderByTest(){
        final Genre genre1 = Generator.generateGenre();
        genre1.setName("horor");
        em.persist(genre1);

        Title title2 = Generator.generateTitle();
        title2.setTitleName("B");
        genre1.addTitle(title2);
        title2.addGenre(genre1);
        em.persist(title2);

        Title title1 = Generator.generateTitle();
        title1.setTitleName("A");
        genre1.addTitle(title1);
        title1.addGenre(genre1);
        em.persist(title1);

        final List<Title> result = dao.findByGenre(genre1);

        Assertions.assertEquals("A", result.get(0).getTitleName());
        Assertions.assertEquals("B", result.get(1).getTitleName());
    }
}

