package cz.cvut.fel.ear.library.dao;

import cz.cvut.fel.ear.library.model.Genre;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

@Repository
public class GenreDao extends BaseDao<Genre>{

    public GenreDao() {
        super(Genre.class);
    }
/*
    public Genre getSpecificGenre(){
        try{
            return em.createNamedQuery("Genre.getSpecificGenre", Genre.class).getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }*/


    public Genre findByName(String name){
        try{
            return em.createNamedQuery("Genre.find", Genre.class)
                    .setParameter("name", name)
                    .getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

}
