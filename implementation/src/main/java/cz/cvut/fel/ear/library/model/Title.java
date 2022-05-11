package cz.cvut.fel.ear.library.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.core.annotation.Order;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "TITLE")
@NamedQueries({
        @NamedQuery(name = "Title.findByGenre", query = "SELECT t FROM Title t WHERE :genre MEMBER OF t.genres order by t.name ASC"),
        @NamedQuery(name = "Title.findByAuthor", query = "SELECT t FROM Title t WHERE t.author = :author order by t.name ASC"),
        @NamedQuery(name = "Title.findByNameAndISBN", query = "SELECT t FROM Title t WHERE t.name = :name AND t.ISBN = :ISBN")
})
public class Title extends AbstractEntity{
    @Column(name = "ISBN")
    private String ISBN;

    @Column(nullable = false)
    private String name;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "title")
    @JoinColumn(name = "bookID")
    @OrderBy("title.name")
    private List<Book> listOfBooks;


    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(nullable = false)
    private Author author;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(nullable = false)
    @OrderBy("genreName")
    private List<Genre> genres;

    public Title() {
    }

    public Title(String name, String isbn) {
        this.ISBN = isbn;
        this.name = name;
    }

    public Title(Author author) {
        this.author = author;
    }

    //GETTERS AND SETTERS

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }


    public String getTitleName() {
        return name;
    }

    public void setTitleName(String name) {
        this.name = name;
    }

    public List<Book> getCopies() {
        return listOfBooks;
    }

    public void setCopies(List<Book> copies) {
        this.listOfBooks = copies;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }


    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public void addBookCopy(Book copy) {
        Objects.requireNonNull(copy);

        if (listOfBooks == null) {
            this.listOfBooks = new ArrayList<>();
        }

        listOfBooks.add(copy);
    }

    //todo remove vykomentovan
//    public void removeBookCopy(Book copy) {
//        Objects.requireNonNull(copy);
//        if (listOfBooks == null) {
//            return;
//        }
//        listOfBooks.removeIf(c -> c.equals(copy));
//
//    }

    public void addGenre(Genre genre) {
        Objects.requireNonNull(genre);

        if (genres == null) {
            genres = new ArrayList<>();
        }

        genres.add(genre);
    }



//    public void removeGenre(Genre genre) {
//        Objects.requireNonNull(genre);
//        if (genres == null) {
//            return;
//        }
//        genres.removeIf(g ->  Objects.equals(g.getId(), genre.getId()));
//    }

}
