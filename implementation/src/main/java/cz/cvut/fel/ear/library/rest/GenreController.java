package cz.cvut.fel.ear.library.rest;

import cz.cvut.fel.ear.library.exception.NotFoundException;
import cz.cvut.fel.ear.library.exception.UserTypeException;
import cz.cvut.fel.ear.library.model.Genre;
import cz.cvut.fel.ear.library.model.Title;
import cz.cvut.fel.ear.library.rest.util.RestUtils;
import cz.cvut.fel.ear.library.service.GenreService;
import cz.cvut.fel.ear.library.service.TitleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/genres")
public class GenreController {
    private static final Logger log = Logger.getLogger(Genre.class.getName());

    private final GenreService service;

    @Autowired
    public GenreController(GenreService service) {
        this.service = service;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Genre> getGenres() {
        log.info("Finding all genres.");

        return service.findAll();
    }

    @GetMapping(value = "/{name}",produces = MediaType.APPLICATION_JSON_VALUE)
    public Genre getGenreByName(@PathVariable @RequestBody String name) {
        log.info("Finding genre by name.");

        return service.findByName(name);
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createGenre(@RequestBody(required = false) Genre genre) {
        log.info("Adding genre " + genre.getName() + ".");

        service.persist(genre);

        final HttpHeaders headers = RestUtils
                .createLocationHeaderFromCurrentUri("/{genre}", genre.getName());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Genre getGenre(@PathVariable Integer id) {
        log.info("Getting genre with ID " + id + ".");

        final Genre genre = service.find(id);

        if (genre == null) {
            log.info("Could not retrieve genre with ID " + id + ".");

            throw new NotFoundException("Genre not found!");
        }

        return genre;
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PutMapping(value = "/update/{genrename}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateGenre(@PathVariable String genrename,
                                               @RequestBody(required = false) Genre genre) {
        log.info("Updating genre with ID " + genrename + ".");

        final Genre genreInDb = service.findByName(genrename);

        if (genreInDb == null) {
            log.info("Could not update genre with ID " + genrename + ".");

            throw new NotFoundException("Genre to update not found!");
        }

        service.update(genre);

        final HttpHeaders header = RestUtils.createLocationHeaderFromCurrentUri("");
        return new ResponseEntity<>(header, HttpStatus.CREATED);
    }

    @GetMapping(value = "/titles/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Title> getGenreTitles(@PathVariable Integer id) {
        log.info("Retrieving a list of titles for genre " + id + ".");

        final Genre genre = service.find(id);

        if (genre == null) {
            log.info("Genre " + id + " not found - could not retrieve titles.");

            throw new NotFoundException("Genre not found!");
        }

        return genre.getTitles();
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PostMapping(value = "/titles/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addTitleToGenre(@PathVariable Integer id,
                                                   @RequestBody(required = false) Title title) {
        log.info("Adding title " + title.getTitleName() + " to genre with ID:  " + id + ".");

        final Genre genre = service.find(id);

        if (genre == null) {
            log.info("Could not add title " + title.getTitleName() + " , because genre does not exist.");

            throw new NotFoundException("Genre doesnt exist!");
        }

        service.addTitle(genre, title);

        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/api/genres/{id}", id);
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
}
