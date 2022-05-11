package cz.cvut.fel.ear.library.rest;


import cz.cvut.fel.ear.library.exception.NotFoundException;
import cz.cvut.fel.ear.library.model.Reservation;
import cz.cvut.fel.ear.library.model.User;
import cz.cvut.fel.ear.library.model.UserType;
import cz.cvut.fel.ear.library.rest.util.RestUtils;
import cz.cvut.fel.ear.library.security.model.AuthenticationToken;
import cz.cvut.fel.ear.library.service.ReservationService;
import cz.cvut.fel.ear.library.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * The type User controller.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger LOG = LoggerFactory.getLogger(AuthorController.class);

    private final UserService service;

    private final ReservationService reservationService;


    @Autowired
    private AuthenticationProvider provider;


    @Autowired
    public UserController(UserService service, ReservationService reservationService) {
        this.service = service;
        this.reservationService = reservationService;
    }


    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getUsers() {
        LOG.info("Retrieving all users.");

        return service.findAll();
    }

    @GetMapping(value = "/username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUser(@PathVariable @RequestBody String username) {
        LOG.info("Retrieving user " + username + ".");

        final User user = service.findByUsername(username);

        if (user == null) {
            LOG.error("Could not retrieve user with this username.");

            throw new NotFoundException("User with username: "+ username + " was not found");
        }

        return user;
    }

    @GetMapping(value = "/{substring}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getUsersWithSubstring(@PathVariable @RequestBody String substring) {
        LOG.info("Retrieving users with substring: " + substring + ".");

        final List<User> users = service.findByPartOfUsername(substring);

        if (users == null) {
            LOG.error("Could not retrieve users with this substring.");

            throw new NotFoundException("Users with substring: "+ substring + " was not found");
        }

        return users;
    }

    @PreAuthorize("(!#user.isLibrarian() && anonymous) || hasRole('ROLE_LIBRARIAN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> register(@RequestBody User user) {
        service.persist(user);

        LOG.debug("User {} successfully registered.", user);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/current");
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ROLE_LIBRARIAN', 'ROLE_GUEST')")
    @GetMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getCurrent(Principal principal) {


        final AuthenticationToken auth = (AuthenticationToken) principal;
        return auth.getPrincipal().getUser();
    }


    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PostMapping(value = "/librarian",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createLibrarian(@RequestBody(required = false) User user) {
        LOG.info("Creating new librarian " + user.getUsername() + ".");
        LOG.debug(user.toString());

        user.setUserType(UserType.LIBRARIAN);
        service.persist(user);

        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{username}", user.getUsername());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PutMapping(value = "/update/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateUser(@PathVariable String username,
                                           @RequestBody(required = false) User user) {
        LOG.info("Updating user " + username + ".");
        LOG.debug(user.toString());

        final User oldValues = service.findByUsername(username);

        if (oldValues == null) {
            LOG.error("Could not retrieve user " + username + ".");

            throw new NotFoundException("User "+ username+ " was not found");
        }

        service.update(user);

        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("");
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{username}/reservations", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Reservation> getUserReservations(@PathVariable @RequestBody String username) {
        LOG.info("Retrieving a list of reservations for user " + username + ".");
        final User user = service.findByUsername(username);

        final List<Reservation> reservation = reservationService.findActiveByUser(user);

        if (reservation.isEmpty()) {
            LOG.error("Could not retrieve reservations of " + username + ".");

            throw new NotFoundException("User "+ username + " reservations were not found");
        }

        return reservation;
    }


}