package cz.cvut.fel.ear.library.dao;

import cz.cvut.fel.ear.library.model.Reservation;
import cz.cvut.fel.ear.library.model.User;
import cz.cvut.fel.ear.library.model.UserType;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.List;

@Repository
public class UserDao extends BaseDao<User> {

    public UserDao(){
        super(User.class);
    }

    public List<User> getOnlyGuests(){
        try{
            return em.createNamedQuery("User.getOnlyGuests", User.class).getResultList();
        }catch (NoResultException e){
            return null;
        }
    }

    public List<User> getOnlyLibrarian(){
        try{
            return em.createNamedQuery("User.getOnlyLibrarian", User.class).getResultList();
        }catch (NoResultException e){
            return null;
        }
    }

    public User findByUsername(String username){
        try {
            return em.createNamedQuery("User.findByUsername", User.class)
//                    .setParameter("username", "%"+username+"%")
                    .setParameter("username", username)
                    .getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }

    public List<User> findByPartOfUsername(String username){
        try {
            return em.createNamedQuery("User.findByPartOfUsername", User.class)
                    .setParameter("username", "%"+username+"%")
                    .getResultList();
        }catch(NoResultException e){
            return null;
        }
    }

}
