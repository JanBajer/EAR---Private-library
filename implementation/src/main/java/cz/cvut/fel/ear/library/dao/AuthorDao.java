package cz.cvut.fel.ear.library.dao;

import cz.cvut.fel.ear.library.model.Author;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Objects;

@Repository
public class AuthorDao extends BaseDao<Author>{

    public AuthorDao() {
        super(Author.class);
    }


    public List<Author> findByFullName(String firstName, String lastName){
        try {
            return em.createNamedQuery("Author.findByNames",Author.class)
                    .setParameter("firstName", firstName)
                    .setParameter("lastName", lastName)
                    .getResultList();
        }catch(NoResultException e){
            return null;
        }
    }

    public List<Author> findAllByFirstName(String firstName){
        try {
            return em.createNamedQuery("Author.findAllByFirstname",Author.class)
                    .setParameter("firstName", "%" + firstName + "%")
                    .getResultList();
        }catch(NoResultException e){
            return null;
        }
    }

    public List<Author> findAllByLastName(String lastName){
        try {
            return em.createNamedQuery("Author.findAllByLastname",Author.class)
                    .setParameter("lastName", "%"+ lastName + "%")
                    .getResultList();
        }catch(NoResultException e){
            return null;
        }
    }
}
