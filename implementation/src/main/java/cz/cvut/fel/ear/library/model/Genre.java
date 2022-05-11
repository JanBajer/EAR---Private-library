package cz.cvut.fel.ear.library.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "GENRE")

@NamedQueries({
        @NamedQuery(name = "Genre.find", query = "SELECT c FROM Genre c WHERE c.genreName = :name")
})
public class Genre extends AbstractEntity{



    @Column(nullable = false,unique = true)
    private String genreName;

    @ManyToMany(mappedBy = "genres", cascade = CascadeType.PERSIST)
    @JsonIgnore
    @OrderBy("name")
    private List<Title> titles;

    public Genre(String genreName){
        this.genreName = genreName;
        this.titles = new ArrayList<>();
    }

    public Genre() {
    }

    public List<Title> getTitles() {
        return titles;
    }

    public void setTitles(List<Title> titles) {
        this.titles = titles;
    }


    public void addTitle(Title title) {
        Objects.requireNonNull(title);
        if (titles == null) {
            titles = new ArrayList<>();
        }
        titles.add(title);
    }

    public void removeTitle(Title title) {
        Objects.requireNonNull(title);
        if (titles == null) {
            return;
        }
        titles.removeIf(t -> t.equals(title));
    }

    public String getName() {
        return genreName;
    }

    public void setName(String name) {
        this.genreName = name;
    }
}
