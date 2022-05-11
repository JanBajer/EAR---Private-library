package cz.cvut.fel.ear.library.model;

import cz.cvut.fel.ear.library.util.Constants;

import javax.persistence.*;
import java.time.LocalDate;


// TODO opravit querries
@Entity
@Table(name = "BOOKLOAN")
@NamedQueries({
        @NamedQuery(name = "Bookloan.findByUser", query = "SELECT r FROM BookLoan r WHERE r.user = :user"),
        @NamedQuery(name = "Bookloan.findAllActive", query = "SELECT r FROM BookLoan r WHERE r.active = true"),
        @NamedQuery(name = "Bookloan.findActiveByDate", query = "SELECT r FROM BookLoan r WHERE r.dateOfLoan <= :date AND r.dateOfSupposedReturn >= :date"),
        @NamedQuery(name = "Bookloan.findAllActiveByBook", query = "SELECT r FROM BookLoan r WHERE r.active = true AND r.book = :book"),
        @NamedQuery(name = "Bookloan.findActiveByUser", query = "SELECT r FROM BookLoan r WHERE r.active = true AND r.user = :user"),
})
public class BookLoan extends BookState {


    @Column(nullable = false)
    private LocalDate dateOfLoan;

    @Column
    private LocalDate dateOfReturn;

    @Column (nullable = false)
    private LocalDate dateOfSupposedReturn;


    public BookLoan(Book book, User user) {
        super(user, book);
        this.book = book;
        this.user = user;
        this.active = true;
        this.dateOfLoan = LocalDate.now();
        this.dateOfSupposedReturn = LocalDate.now().plusDays(Constants.DAYS_OF_BOOKLOAN);
        this.dateOfReturn = null;

    }

    public BookLoan() {

    }


    public LocalDate getDateOfLoan() {
        return dateOfLoan;
    }

    public void setDateOfLoan(LocalDate dateOfLoan) {
        this.dateOfLoan = dateOfLoan;
    }

    public LocalDate getDateOfSupposedReturn() {
        return dateOfSupposedReturn;
    }

    public void setDateOfSupposedReturn(LocalDate dateOfReturn) {
        this.dateOfSupposedReturn = dateOfReturn;
    }

    public LocalDate getDateOfReturn() {
        return dateOfReturn;
    }

    public void setDateOfReturn(LocalDate dateOfReturn) {
        this.dateOfReturn = dateOfReturn;
    }
}
