package com.javamentor.springbootstrap.dao;

import com.javamentor.springbootstrap.model.Role;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collection;

@Repository
public class RoleDoaImpl implements RoleDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Collection<Role> getRoles() {
        TypedQuery<Role> query = em.createQuery("SELECT r from Role r", Role.class);
        return query.getResultList();
    }

    @Override
    public Collection<Role> getRoles(String roleName) {
        TypedQuery<Role> query = em.createQuery("SELECT r from Role r WHERE r.role = :roleName", Role.class);
        query.setParameter("roleName", roleName);
        return query.getResultList();
    }
}
