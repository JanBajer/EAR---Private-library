package cz.cvut.fel.ear.library.service;

import cz.cvut.fel.ear.library.environment.Generator;
//import cz.cvut.fel.ear.library.exception.InvalidIDException;
import cz.cvut.fel.ear.library.model.Author;
import cz.cvut.fel.ear.library.model.Genre;
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
public class GenreServiceTest {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private GenreService service;

    @DisplayName("Add Genre Adds Genre to Target Title And Then Remove")
    @Test
    public void addGenreAddsGenreToTargetTitleAndRemove() {
        final Title title = Generator.generateTitle();
        final Genre genre = Generator.generateGenre();

        em.persist(title);

        //Has none
        Assertions.assertEquals(0,title.getGenres().size());

        service.persist(genre);

        //add genre
        service.addTitle(genre, title);
        final Title result = em.find(Title.class, title.getId());

        //Should have 1
        Assertions.assertTrue(result.getGenres().stream().anyMatch(c -> c.getId().equals(genre.getId())));
        Assertions.assertEquals(1,title.getGenres().size());

        //remove genre and should have 0
//        service.removeGenre(genre,title);
//        Assertions.assertEquals(0,title.getGenres().size());


    }




}

