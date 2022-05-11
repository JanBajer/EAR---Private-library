package cz.cvut.fel.ear.library.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@MappedSuperclass
public abstract class BookState extends AbstractEntity{

    @Column(nullable = false)
    protected boolean active;


    @ManyToOne(cascade = CascadeType.MERGE)
    protected User user;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    protected Book book;

    public BookState() {
    }

    public BookState(User user, Book book) {
        this.user = user;
        this.book = book;
        this.active = true;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
