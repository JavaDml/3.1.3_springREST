package com.javamentor.springbootstrap.dao;

import com.javamentor.springbootstrap.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import com.javamentor.springbootstrap.model.Role;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private PasswordEncoder passEnc;

    @Override
    public void addOrEditUser(User User) {
        em.merge(User);
    }

    @Override
    public boolean delUser(User User) {
        Query query = em.createQuery("DELETE FROM User u WHERE u.id = :id");
        query.setParameter("id", User.getId());
        query.executeUpdate();
        return true;
    }

    @Override
    public User getUser(Long id) {
        return em.find(User.class, id);
    }

    @Override
    public List<User> getUsers() {
        return em.createQuery("SELECT u from User u", User.class).getResultList();
    }

    @Override
    public User getUserByName(String s) {
        try {
            TypedQuery<User> query = em.createQuery("SELECT u from User u WHERE u.name = :name", User.class);
            query.setParameter("name", s);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void createDataTables() {
        Role userRole = new Role("ROLE_USER");
        Role adminRole = new Role("ROLE_ADMIN");
        em.persist(new User("Tom", "Anderson", 54, passEnc.encode("12345"), new HashSet<>(){{add(userRole);}}));
        em.persist(new User("Mike", "Kyk", 36, passEnc.encode("123"), new HashSet<>(){{add(userRole); add(adminRole);}}));
        em.persist(new User("Ivan", "Petrov", 20, passEnc.encode("000"), new HashSet<>(){{add(adminRole);}}));
    }

}
