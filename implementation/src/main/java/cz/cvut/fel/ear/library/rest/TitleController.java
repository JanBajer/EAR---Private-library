package cz.cvut.fel.ear.library.rest;

import cz.cvut.fel.ear.library.exception.NotFoundException;
import cz.cvut.fel.ear.library.model.Author;
import cz.cvut.fel.ear.library.model.Book;
import cz.cvut.fel.ear.library.model.Genre;
import cz.cvut.fel.ear.library.model.Title;
import cz.cvut.fel.ear.library.rest.util.RestUtils;
import cz.cvut.fel.ear.library.service.AuthorService;
import cz.cvut.fel.ear.library.service.TitleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping(value = "/api/titles")
public class TitleController {
    private static final Logger LOG = LoggerFactory.getLogger(TitleController.class);

    private final TitleService service;
    private final AuthorService authorService;

    @Autowired
    public TitleController(TitleService service, AuthorService authorService) {
        this.service = service;
        this.authorService = authorService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Title> getAllTitles(HttpServletRequest request) {
        LOG.info("Retrieving all titles.");

        return service.findAll();
    }


    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Void> createTitle(@RequestBody(required = false) Title title) {
    public ResponseEntity<Void> createTitle(@RequestBody(required = false) String name, String isbn, int authorId) {

        Title title = new Title(name, isbn);

        Author a = authorService.find(authorId);
        if (a == null){
            throw new NotFoundException("Author not found");
        }

        a.addTitle(title);
        title.setAuthor(a);


        LOG.info("Creating new title " + title.getTitleName() + ".");
        LOG.debug(title.toString());

        final Author author = authorService.find(title.getAuthor().getId());

        if (author == null) {
            LOG.error("Could not retrieve non-existent author " + title.getAuthor().getId() + ".");

            throw new NotFoundException("Author not doesnt exist");
        }

        service.persist(title);

        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", title.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Title getSpecificTitleById(@PathVariable Integer id) {
        LOG.info("Retrieving title " + id + ".");

        final Title title = service.find(id);

        if (title == null) {
            LOG.error("Could not retrieve non-existent title." + id +".");

            throw new NotFoundException("Title not doesnt exist");
        }

        return title;
    }

    @GetMapping(value = "/{genre}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Title> getTitlesByGenre(@PathVariable @RequestBody Genre genre) {
        LOG.info("Retrieving titles with " + genre + ".");

        final List<Title> title = service.findByGenre(genre);

        if (title == null) {
            LOG.error("Could not retrieve any titles with " + genre +".");

            throw new NotFoundException("Title with " + genre + " was not found");
        }

        return title;
    }

    @GetMapping(value = "/{author.firstName}/{author.lastName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Title> getTitlesByAuthor(@PathVariable @RequestBody Author author) {
        LOG.info("Retrieving titles written by " + author + ".");

        final List<Title> title = service.findByAuthor(author);

        if (title == null) {
            LOG.error("Could not retrieve any titles by " + author +".");

            throw new NotFoundException("Any title written by "+ author+ " was not found");
        }

        return title;
    }

    @GetMapping(value = "/{id}/books", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Book> getCopies(@PathVariable Integer id) {
        LOG.info("Retrieving books of title " + id + ".");

        final Title title = service.find(id);

        if (title == null) {
            LOG.error("Could not retrieve non-existent title.");

            throw new NotFoundException("Title not doesnt exist");
        }

        return title.getCopies();
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PostMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateTitle(@PathVariable Integer id,
                                            @RequestBody Title title) {
        LOG.info("Updating title " + id + ".");

        final Title oldValues = service.find(title.getId());

        if (oldValues == null) {
            LOG.error("Could not retrieve non-existent title " + id + ".");

            throw new NotFoundException("Title not doesnt exist");
        }

        service.update(title);

        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("");
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

}
