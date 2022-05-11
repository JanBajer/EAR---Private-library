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

@DisplayName("BookLoanService Test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class BookLoanServiceTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private BookLoanService service;

    @Test
    public void lendBook_bookChangesBookState(){
        Book book = Generator.generateBook();
        User user = Generator.generateUser();
        user.setUserType(UserType.GUEST);
        book.setBookState(BookStateType.AVAILABLE);
//        book.setBookLoans(null);
        em.persist(book);
        em.persist(user);

        BookLoan result = service.lendBook(book, user);

        assertEquals(book, result.getBook());
        assertEquals(BookStateType.LEND, result.getBook().getBookState());
        assertEquals(user, result.getUser());
        assertNotNull(result.getBook().getBookLoans());
    }

    @Test
    public void returnBook_updatesUserAndBookAfterReturningBook(){
        BookLoan bookLoan = Generator.generateBookLoan();
        User user = bookLoan.getUser();
        Book book = bookLoan.getBook();

        em.persist(bookLoan);

        service.returnBook(bookLoan);

        assertFalse(em.find(BookLoan.class, bookLoan.getId()).isActive());
        assertFalse(book.getBookLoans().get(0).isActive());
//        assertFalse(user.getListOfLoanBooks().contains(bookLoan));
    }
}
