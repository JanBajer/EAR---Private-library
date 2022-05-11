package cz.cvut.fel.ear.library.rest;

import cz.cvut.fel.ear.library.exception.NotFoundException;
import cz.cvut.fel.ear.library.model.*;
import cz.cvut.fel.ear.library.rest.util.RestUtils;
import cz.cvut.fel.ear.library.service.AuthorService;
import cz.cvut.fel.ear.library.service.BookService;
import cz.cvut.fel.ear.library.service.TitleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private static final Logger log = Logger.getLogger(Genre.class.getName());

    private final BookService service;
    private final TitleService titleService;
    private final AuthorService authorService;

    @Autowired
    public BookController(BookService service, TitleService titleService, AuthorService authorService) {
        this.service = service;
        this.titleService = titleService;
        this.authorService = authorService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Book> getAllBooks() {
        log.info("Finding every book in library.");

        return service.findAll();
    }

    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public Book getBook(@PathVariable Integer id) {
        log.info("Finding book by id.");

        return service.find(id);
    }

    @GetMapping(value = "/isbn/{ISBN}",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Book> getBooksWithISBN(@PathVariable String ISBN) {
        log.info("Finding books with ISBN: " + ISBN + ".");

        return service.findAllByISBN(ISBN);
    }

    @GetMapping(value = "/title/{title.id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Book> getBooksWithISBN(@PathVariable Title title) {
        log.info("Finding books of title: " + title.getTitleName() + ".");

        return service.findAllByTitle(title);
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Void> addBook(@PathVariable Book book) {
    public ResponseEntity<Void> addBook(String name, String isbn) {
        Book book = new Book();
        Title title = titleService.findByName(name, isbn);
        if (title == null){
            throw new NotFoundException("Title not found!");
        }
        book.setTitle(title);
        book.setBookState(BookStateType.AVAILABLE);

        log.info("Adding new book of title: " + book.getTitle().getTitleName() + ".");

        service.persist(book);

        final HttpHeaders headers = RestUtils
                .createLocationHeaderFromCurrentUri("/{id}", book.getBookID());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateBook(@RequestBody Book book) {
        log.info("Updating book with ID " + book.getBookID() + ".");

        final Book bookInDb = service.find(book.getBookID());

        if (bookInDb == null) {
            log.info("Could not update book with ID " + book.getBookID() + ".");

            throw new NotFoundException("Book to update not found!");
        }

        service.update(book);

        final HttpHeaders header = RestUtils.createLocationHeaderFromCurrentUri("");
        return new ResponseEntity<>(header, HttpStatus.CREATED);
    }

}
