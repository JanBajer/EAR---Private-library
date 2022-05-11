package cz.cvut.fel.ear.library.rest;

import cz.cvut.fel.ear.library.exception.BookNotAvailable;
import cz.cvut.fel.ear.library.exception.NotFoundException;
import cz.cvut.fel.ear.library.model.*;
import cz.cvut.fel.ear.library.rest.util.RestUtils;
import cz.cvut.fel.ear.library.service.BookLoanService;
import cz.cvut.fel.ear.library.service.BookService;
import cz.cvut.fel.ear.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/api/bookloans")
public class BookLoanController {
    private static final Logger log = Logger.getLogger(Genre.class.getName());

    private final BookLoanService service;
    private final BookService bookService;
    private final UserService userService;

    @Autowired
    public BookLoanController(BookLoanService service, BookService bookService, UserService userService) {
        this.service = service;
        this.bookService = bookService;
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Void> createBookLoan(@RequestBody Book book, @RequestBody User user) {
    public ResponseEntity<Void> createBookLoan(int bookID, int userID) {

        Book book = bookService.find(bookID);
        if (book == null){
            throw new NotFoundException("Book not found!");
        }
        User user = userService.find(userID);
        if (user == null){
            throw new NotFoundException("User not found");
        }

        log.info("Creating new bookloan for user " + user.getUsername() + " with book: " + book.getBookID() + ".");

        try {
            BookLoan bookLoan =  service.lendBook(book , user);

            final HttpHeaders headers = RestUtils
                    .createLocationHeaderFromCurrentUri("/{id}", bookLoan.getId());

            return new ResponseEntity<>(headers, HttpStatus.CREATED);
        }catch (BookNotAvailable e){
            log.info("Book loan denied - there already exist book loan or reservation for chosen book!");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
//    @PostMapping(value = "/bookLoan.id", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Void> returnBook(@PathVariable @RequestBody BookLoan bookLoan){
    @PutMapping(value = "/{bookloanID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> returnBook(@PathVariable int bookloanID){

        BookLoan bookLoan = service.find(bookloanID);
        if (bookLoan == null) throw new NotFoundException("BookLoan not found");

        log.info("Returning book: " + bookLoan.getBook().getBookID());

        service.returnBook(bookLoan);

        final HttpHeaders header = RestUtils.createLocationHeaderFromCurrentUri("");
        return new ResponseEntity<>(header, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @GetMapping(value = "/check/{book.id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean checkAvailability(@PathVariable @RequestBody Book book){

        log.info("Checking if book can be lend");

        return service.isAvailable(book);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public BookLoan getBookLoan(@PathVariable Integer id) {
        log.info("Getting bookloan with ID " + id + ".");

        final BookLoan bookLoan = service.find(id);

        if (bookLoan == null) {
            log.info("Could not retrieve bookloan with ID " + id + ".");

            throw new NotFoundException("BookLoan not found!");
        }

        return bookLoan;
    }

}
