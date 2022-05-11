package cz.cvut.fel.ear.library.rest;

import cz.cvut.fel.ear.library.exception.NotFoundException;
import cz.cvut.fel.ear.library.model.Author;
import cz.cvut.fel.ear.library.model.Title;
import cz.cvut.fel.ear.library.rest.util.RestUtils;
import cz.cvut.fel.ear.library.service.AuthorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {
    private static final Logger LOG = LoggerFactory.getLogger(AuthorController.class);

    private final AuthorService service;

    @Autowired
    public AuthorController(AuthorService service) {
        this.service = service;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Author> getAuthors() {
        LOG.info("Retrieving all authors.");

        return service.findAll();
    }

    @GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Author getAuthor(@PathVariable @RequestBody Integer id) {
        LOG.info("Retrieving author " + id + ".");

        final Author author = service.find(id);

        if (author == null) {
            LOG.error("Author " + id + " not found - could not retrieve non-existent author.");

            throw new NotFoundException("Author not found");
        }

        return author;
    }

    @GetMapping(value = "/{id}/titles", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Title> getAuthorTitles(@PathVariable Integer id) {
        LOG.info("Retrieving titles of author " + id + ".");

        final Author author = service.find(id);

        if (author == null) {
            LOG.error("Author " + id + " not found - could not retrieve titles.");

            throw new NotFoundException("Author not found");
        }

        return author.getTitles();
    }

    @GetMapping(value = "/{firstName}/{lastName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Author> getAuthorsByFullName(@PathVariable String firstName,@PathVariable String lastName) {
        LOG.info("Retrieving titles written by " + firstName + lastName + ".");

        final List<Author> author = service.findByFullName(firstName,lastName);

        if (author == null) {
            LOG.error("Could not retrieve any author " + firstName+lastName +".");

            throw new NotFoundException("Title written by "+ firstName+lastName+" was not found.");
        }

        return author;
    }

    @GetMapping(value = "/{firstName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Author> getAuthorsByFirstName(@PathVariable String firstName) {
        LOG.info("Retrieving titles written by authors with " + firstName + ".");

        final List<Author> author = service.findAllByFirstName(firstName);

        if (author == null) {
            LOG.error("Could not retrieve any author " + firstName+".");

            throw new NotFoundException("Title written by "+ firstName+" was not found.");
        }

        return author;
    }

    @GetMapping(value = "/{lastName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Author> getAuthorsByLastName(@PathVariable String lastName) {
        LOG.info("Retrieving titles written by authors with " + lastName + ".");

        final List<Author> author = service.findAllByLastName(lastName);

        if (author == null) {
            LOG.error("Could not retrieve any author " + lastName+".");

            throw new NotFoundException("Title written by "+ lastName+" was not found.");
        }

        return author;
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createNewAuthor(String firstname, String lastname) {

        Author author = new Author(firstname, lastname);

        LOG.info("Adding new author " + author.getFirstName() + " " + author.getLastName() + ".");
        LOG.debug(author.toString());

        service.persist(author);

        final HttpHeaders header = RestUtils.createLocationHeaderFromCurrentUri("/{id}", author.getId());
        return new ResponseEntity<>(header, HttpStatus.CREATED);
    }


    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateAuthor(@PathVariable Integer id,
                                             @RequestBody(required = false) Author author) {
        LOG.info("Updating author " + id + ".");
        LOG.debug(author.toString());

        final Author oldValues = service.find(id);

        if (oldValues == null) {
            LOG.error("Author " + id + " not found - could not retrieve author.");

            throw new NotFoundException("Author not found");
        }

        service.update(author);

        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("");
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

}
