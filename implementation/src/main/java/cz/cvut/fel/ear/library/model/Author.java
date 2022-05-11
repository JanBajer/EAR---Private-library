package cz.cvut.fel.ear.library.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "AUTHOR")
@NamedQueries({
        @NamedQuery(name = "Author.findByNames", query = "SELECT a FROM Author a WHERE a.firstName = :firstName AND a.lastName = :lastName "),
        @NamedQuery(name = "Author.findAllByFirstname", query = "SELECT a FROM Author a WHERE a.firstName LIKE  :firstName"),
        @NamedQuery(name = "Author.findAllByLastname", query = "SELECT a FROM Author a WHERE a.lastName LIKE :lastName ")
})
public class Author extends AbstractEntity{

    private String firstName;

    private String lastName;

    @OneToMany(mappedBy = "author", cascade = CascadeType.PERSIST)
    @OrderBy("name")
    @JsonIgnore
    private List<Title> titles;

    public Author(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Author() {
    }

    //GETTERS AND SETTERS


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Title> getTitles() {
        return titles;
    }


    public void setTitles(List<Title> writtenBooks) {
        this.titles = writtenBooks;
    }

    public void addTitle(Title title) {
        Objects.requireNonNull(title);
        if (titles == null) {
            titles = new ArrayList<>();
        }
        titles.add(title);
    }

//    public void removeTitle(Title title) {
//        Objects.requireNonNull(title);
//        if (titles == null) {
//            return;
//        }
//        titles.removeIf(t -> t.equals(title));
//    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass().equals(Author.class)) {
            Author compared = (Author) obj;
            return firstName.equals(compared.firstName) && lastName.equals(compared.lastName)  && titles.equals(compared.titles);
        } else {
            return false;
        }
    }


}
