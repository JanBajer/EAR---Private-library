package cz.cvut.fel.ear.library.rest;

import cz.cvut.fel.ear.library.exception.BookNotAvailable;
import cz.cvut.fel.ear.library.exception.NotFoundException;
import cz.cvut.fel.ear.library.model.Book;
import cz.cvut.fel.ear.library.model.Genre;
import cz.cvut.fel.ear.library.model.Reservation;
import cz.cvut.fel.ear.library.model.User;
import cz.cvut.fel.ear.library.rest.util.RestUtils;
import cz.cvut.fel.ear.library.security.model.AuthenticationToken;
import cz.cvut.fel.ear.library.service.BookService;
import cz.cvut.fel.ear.library.service.ReservationService;
import cz.cvut.fel.ear.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/api/reservations")
public class ReservationController {

    private static final Logger log = Logger.getLogger(Genre.class.getName());

    private final ReservationService service;
    private final BookService bookService;
    private final UserService userService;

    @Autowired
    public ReservationController(ReservationService service, BookService bookService, UserService userService) {
        this.service = service;
        this.bookService = bookService;
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ROLE_GUEST')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Void> createReservation(@RequestBody Book book, @RequestBody User user) {
    public ResponseEntity<Void> createReservation(int bookID, Principal principal) {

        AuthenticationToken curr = (AuthenticationToken) principal;
        User currUser = curr.getPrincipal().getUser();

        Book book = bookService.find(bookID);
        if (book == null){
            throw new NotFoundException("Book not found!");
        }
        User user = userService.find(currUser.getId());
        if (user == null){
            throw new NotFoundException("User not found");
        }

        log.info("Creating new reservation for user " + user.getUsername() + " and book: " + book.getBookID() + ".");

        try {
            Reservation reservation =  service.create(book , user);

            final HttpHeaders headers = RestUtils
                    .createLocationHeaderFromCurrentUri("/{id}", reservation.getId());

            return new ResponseEntity<>(headers, HttpStatus.CREATED);
        }catch (BookNotAvailable e){
            log.info("Reservation denied - there already exist reservation for chosen book!");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Void> cancelReservation(@RequestBody Reservation reservation){
    public ResponseEntity<Void> cancelReservation(int reservationId){
        Reservation reservation = service.find(reservationId);
        if (reservation == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        log.info("Canceling reservation: " + reservation.getId());

        service.cancel(reservation);

        final HttpHeaders header = RestUtils.createLocationHeaderFromCurrentUri("");
        return new ResponseEntity<>(header, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean checkReservationStatus(@RequestBody Reservation reservation){
        log.info("Checking reservation status");

        return service.checkIfShouldBeActive(reservation);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Reservation getReservation(@PathVariable Integer id) {
        log.info("Getting reservation with ID " + id + ".");

        final Reservation reservation = service.find(id);

        if (reservation == null) {
            log.info("Could not retrieve reservation with ID " + id + ".");

            throw new NotFoundException("Reservation not found!");
        }

        return reservation;
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @DeleteMapping(value = "/{reservation.id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deactivateReservation(@PathVariable @RequestBody Reservation reservation){
        log.info("Changing reservation from active to not active");

        service.changeReservationState(reservation);
    }

}
