package cz.cvut.fel.ear.library.dao;

import cz.cvut.fel.ear.library.Library;
import cz.cvut.fel.ear.library.environment.Generator;
import cz.cvut.fel.ear.library.model.Genre;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DisplayName("GenreDao Test")
@ExtendWith(SpringExtension.class)
@DataJpaTest
@ComponentScan(basePackageClasses = Library.class)
public class GenreDaoTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private GenreDao dao;

    @DisplayName("Find By Name Should Retrieve Genre By It's Identifier(=Name)")
    @Test
    public void findByNameRetrievesGenreByItsIdentifier() {
        final Genre genre = Generator.generateGenre();
        em.persist(genre);

        final Genre result = dao.findByName(genre.getName());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(genre, result);
    }

    @DisplayName("Find By Name Should Not Retrieve Non-Existent Genre")
    @Test
    public void findShouldNotRetrieveNonExistentGenre() {
        em.persist(Generator.generateGenre());
        final Genre result = dao.findByName(Generator.generateGenre().getName());
        Assertions.assertNull(result);
    }



}

