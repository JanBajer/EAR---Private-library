package cz.cvut.fel.ear.library.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="LIBRARY_USER")

@SqlResultSetMapping(
        name="UserResult",
        classes = {
                @ConstructorResult(
                        targetClass = User.class,
                        columns = {
                                @ColumnResult(name = "username",type=String.class),
                                @ColumnResult(name = "password",type=String.class),
                                @ColumnResult(name = "userType",type=String.class)
                        }
                )
        }
)
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "User.getOnlyGuests",
                query = "SELECT * FROM LIBRARY_USER WHERE userType = 'GUEST'",
                resultSetMapping = "UserResult"
        ),
        @NamedNativeQuery(
                name = "User.getOnlyLibrarian",
                query = "SELECT * FROM LIBRARY_USER WHERE userType = 'LIBRARIAN'",
                resultSetMapping = "UserResult"
        )
})

@NamedQueries({
        @NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :username"),
        @NamedQuery(name = "User.findByPartOfUsername", query = "SELECT u FROM User u WHERE u.username LIKE :username order by u.username ASC ")
})
public class User extends AbstractEntity{


    @Basic(optional = false)
    @Column(nullable = false, unique = true)
    private String username;

    @Basic(optional = false)
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserType userType;


    public User(){
    }

    public User(String username, String password, String userType) {
        this.username = username;
        this.password = password;
        if (userType.equals(UserType.GUEST.getUsername())){
            this.userType = UserType.GUEST;
        }else if(userType.equals(UserType.LIBRARIAN.getUsername())){
            this.userType = UserType.LIBRARIAN;
        }else{
            userType = null;
        }
    }


    //GETTERS AND SETTERS

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void erasePassword() { this.password = null; }


    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    @JsonIgnore
    public boolean isLibrarian(){
        return userType == UserType.LIBRARIAN;
    }

    @JsonIgnore
    public boolean isGuest(){
        return userType == UserType.GUEST;
    }

    public void encodePassword(PasswordEncoder encoder) {
        this.password = encoder.encode(password);
    }


}
