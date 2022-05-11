package cz.cvut.fel.ear.library.environment;
import cz.cvut.fel.ear.library.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;


import java.util.concurrent.ThreadLocalRandom;

import static org.mockito.Mockito.mock;

public class Generator {

    private static final Random RAND = new Random();

    public static int randomInt() {
        return RAND.nextInt();
    }

    public static boolean randomBoolean() {
        return RAND.nextBoolean();
    }

    public static Author generateAuthor() {
        final Author author = new Author();
        author.setId(randomInt());
        author.setFirstName("FirstName " + randomInt());
        author.setLastName("LastName " + randomInt());
        author.setTitles(new ArrayList<>());
        return author;
    }

    public static Title generateTitle() {
        final Title title = new Title();
        title.setId(randomInt());
        title.setISBN("ISBN " + randomInt());
        title.setTitleName("Title " + randomInt());
        title.setGenres(new ArrayList<>());
        title.setAuthor(generateAuthor());
        return title;
    }

    public static Book generateBook() {
        final Book book = new Book();
        book.setBookID(randomInt());
        book.setBookState(BookStateType.AVAILABLE);
        book.setTitle(generateTitle());
        //book.setReservedBook(new Reservation());

        return book;
    }

    public static Genre generateGenre() {
        final Genre genre = new Genre();
        genre.setId(Generator.randomInt());
        genre.setName("Genre " + Generator.randomInt());
        genre.setTitles(new ArrayList<>());

        return genre;
    }


    public static Reservation generateReservation() {
        Reservation reservation = new Reservation();
        reservation.setId(Generator.randomInt());
        reservation.setActive(true);

        LocalDate rndDate = generateRandomDate();
        reservation.setDateOfReservation(rndDate);
        reservation.setDateOfExpiration(rndDate.plusDays(5));

        Book book = generateBook();
        book.setBookState(BookStateType.RESERVED);
        book.setTitle(generateTitle());
        book.addReservation(reservation);
        reservation.setBook(book);

        User user = generateUser();
//        user.addReservedBook(reservation);
        reservation.setUser(user);

        return reservation;
    }

    public static User generateUser() {
        User user = new User();
        user.setUsername("User " + randomInt());
        user.setPassword("Pass" + randomInt());
        user.setUserType(UserType.GUEST);

        return user;
    }

    public static BookLoan generateBookLoan(){
        BookLoan bl = new BookLoan();
        bl.setActive(true);
        LocalDate rndDate = generateRandomDate();
        bl.setDateOfLoan(rndDate);
        bl.setDateOfSupposedReturn(rndDate.plusDays(30));

        Book book = generateBook();
        book.setBookState(BookStateType.LEND);
        book.setTitle(generateTitle());
        book.addBookloan(bl);
        bl.setBook(book);

        User user = generateUser();
//        user.addBookLoan(bl);
        bl.setUser(user);
        return bl;
    }

    public static LocalDate generateRandomDate(){
        long minDay = LocalDate.of(2000, 1, 1).toEpochDay();
        long maxDay = LocalDate.of(2021, 12, 31).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        return LocalDate.ofEpochDay(randomDay);

    }

}
