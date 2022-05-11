package cz.cvut.fel.ear.library.service.security;


import cz.cvut.fel.ear.library.dao.UserDao;
import cz.cvut.fel.ear.library.model.User;
import cz.cvut.fel.ear.library.security.model.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * The type User details service.
 */
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserDao userDao;

    /**
     * Instantiates a new User details service.
     *
     * @param userDao the user dao
     */
    @Autowired
    public UserDetailsService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        final User user = userDao.findByUsername(s);

        if (user == null) {
            throw new UsernameNotFoundException("User with name " + s + " was not found!");
        }

        return new UserDetails(user);
    }
}
