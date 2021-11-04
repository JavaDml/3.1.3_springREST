package com.javamentor.springbootstrap.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import com.javamentor.springbootstrap.model.Role;
import com.javamentor.springbootstrap.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.HashSet;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private PasswordEncoder passEnc;

    @Override
    public void addOrEditUser(User user) {
        em.merge(user);
    }

    @Override
    public boolean delUser(User user) {
        Query query = em.createQuery("DELETE FROM User u WHERE u.id = :id");
        query.setParameter("id", user.getId());
        query.executeUpdate();
        return true;
    }

    @Override
    public User getUser(Long id) {
        return em.find(User.class, id);
    }

    @Override
    public List<User> getUsers() {
        return em.createQuery("SELECT u from User u",User.class).getResultList();
    }

    @Override
    public User getUserByName(String s) {
        TypedQuery<User> query = em.createQuery("SELECT u from User u WHERE u.name = :name", User.class);
        query.setParameter("name", s);
        return query.getSingleResult();
    }

    @Override
    public Role getRole(String roleName) {
        TypedQuery<Role> query = em.createQuery("SELECT r from Role r WHERE r.role = :roleName", Role.class);
        query.setParameter("roleName", roleName);
        return query.getSingleResult();
    }

    @Override
    public void createDataTables() {
        Role userRole = new Role("ROLE_USER");
        Role adminRole = new Role("ROLE_ADMIN");
        em.persist(new User("Tom", "Anderson", 54, passEnc.encode("12345"), new HashSet<Role>(){{add(userRole);}}));
        em.persist(new User("Mike", "Kyk", 36, passEnc.encode("123"), new HashSet<Role>(){{add(userRole); add(adminRole);}}));
        em.persist(new User("Ivan", "Petrov", 20, passEnc.encode("000"), new HashSet<Role>(){{add(adminRole);}}));
    }

}
