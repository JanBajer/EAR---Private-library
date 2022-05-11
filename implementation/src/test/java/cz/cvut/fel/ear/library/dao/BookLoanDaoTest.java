package cz.cvut.fel.ear.library.dao;


import cz.cvut.fel.ear.library.Library;
import cz.cvut.fel.ear.library.environment.Generator;
import cz.cvut.fel.ear.library.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@DisplayName("BookLoanDao Test")
@ExtendWith(SpringExtension.class)
@DataJpaTest
@ComponentScan(basePackageClasses = Library.class)
public class BookLoanDaoTest {
    @Autowired
    private TestEntityManager em;
    @Autowired
    private BookLoanDao dao;

    @Test
    public void findByUser_findsBookLoanByUser(){
        BookLoan bookLoan = Generator.generateBookLoan();
        User user = Generator.generateUser();

        bookLoan.setUser(user);
//        user.addBookLoan(bookLoan);
//
        em.persist(bookLoan);
        em.persist(bookLoan.getUser());
        em.persist(bookLoan.getBook());
        em.persist(bookLoan.getBook().getTitle());


        List<BookLoan> result = dao.findByUser(user);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(bookLoan, result.get(0));
    }

    @Test
    public void findActiveByDate_returnsActiveBookLoansForSpecificDate(){
        BookLoan bl1 = Generator.generateBookLoan();
        BookLoan bl2 = Generator.generateBookLoan();
        BookLoan bl3 = Generator.generateBookLoan();

        bl1.setDateOfLoan(LocalDate.of(2020, 7, 11));
        bl2.setDateOfLoan(LocalDate.of(2020, 7, 12));
        bl3.setDateOfLoan(LocalDate.of(2020, 5, 13));


        List<BookLoan> bookLoans = new ArrayList<>();
        bookLoans.add(bl1);
        bookLoans.add(bl2);
        bookLoans.add(bl3);
        for (BookLoan b : bookLoans){
            b.setDateOfSupposedReturn(b.getDateOfLoan().plusDays(30));
            em.persist(b);
            em.persist(b.getUser());
            em.persist(b.getBook());
            em.persist(b.getBook().getTitle());
        }

        List<BookLoan> result = dao.findActiveByDate(LocalDate.of(2020, 7, 12));

        assertNotNull(result);
        assertEquals(2,result.size());
        assertTrue(result.contains(bl1));
        assertTrue(result.contains(bl2));
        assertFalse(result.contains(bl3));
    }

    @Test
    public void findAllActive_retrievesAllActiveBookLoans(){
        BookLoan bl1 = Generator.generateBookLoan();
        BookLoan bl2 = Generator.generateBookLoan();
        BookLoan bl3 = Generator.generateBookLoan();
        bl1.setActive(true);
        bl2.setActive(true);
        bl3.setActive(false);
        List<BookLoan> bookLoans = new ArrayList<>();
        bookLoans.add(bl1);
        bookLoans.add(bl2);
        bookLoans.add(bl3);
        for (BookLoan b : bookLoans){
            em.persist(b);
            em.persist(b.getUser());
            em.persist(b.getBook());
            em.persist(b.getBook().getTitle());
        }

        List<BookLoan> result = dao.findAllActive();

        assertNotNull(result);
        assertTrue(result.contains(bl1));
        assertTrue(result.contains(bl2));
        assertFalse(result.contains(bl3));
    }
}
