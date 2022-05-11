package cz.cvut.fel.ear.library.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "LIBRARY_BOOK")
@NamedQueries({
        @NamedQuery(name = "Book.find", query = "SELECT b FROM Book b WHERE b.bookID = :copyId" ),
        @NamedQuery(name = "Book.findAllByISBN", query = "SELECT b FROM Book b WHERE b.title.ISBN = :ISBN"),
        @NamedQuery(name = "Book.findAllByTitle", query = "SELECT b FROM Book b WHERE b.title = :title")
})
public class Book{

    @Id
    @GeneratedValue
    private Integer bookID;

    @Column(nullable = false)
    private BookStateType bookStateType;

    @OneToMany(cascade = CascadeType.PERSIST)
    @OrderBy("dateOfReservation")
    @JsonIgnore
    private List<Reservation> reservedBooks;

    @OneToMany(cascade = CascadeType.PERSIST)
    @OrderBy("dateOfLoan")
    @JsonIgnore
    private List<BookLoan> bookLoans;

    @ManyToOne(cascade = CascadeType.PERSIST)
//    @MapsId
    @JoinColumn(nullable = false)
    private Title title;

    public void addReservation(Reservation reservation){
        if (reservedBooks == null){
            reservedBooks = new ArrayList<>();
        }

        reservedBooks.add(reservation);
    }

    public void addBookloan(BookLoan bookLoan){
        if (bookLoans == null){
            bookLoans = new ArrayList<>();
        }
        bookLoans.add(bookLoan);
    }

    public Integer getBookID() {
        return bookID;
    }

    public void setBookID(Integer bookID) {
        this.bookID = bookID;
    }

    public BookStateType getBookState() {
        return bookStateType;
    }

    public void setBookState(BookStateType bookStateType) {
        this.bookStateType = bookStateType;
    }


    public List<Reservation> getReservedBook() {
        return reservedBooks;
    }


    public List<BookLoan> getBookLoans() {
        return bookLoans;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }
}
