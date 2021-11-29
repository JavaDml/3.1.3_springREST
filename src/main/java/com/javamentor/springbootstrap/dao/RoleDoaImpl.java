package com.javamentor.springbootstrap.dao;

import com.javamentor.springbootstrap.model.Role;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.HashSet;
import java.util.Set;

@Repository
public class RoleDoaImpl implements RoleDao {

    @PersistenceContext
    private final EntityManager em;

    public RoleDoaImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Set<Role> getRoles() {
        TypedQuery<Role> query = em.createQuery("SELECT r from Role r", Role.class);
        return new HashSet<>(query.getResultList());
    }

    @Override
    public Set<Role> getRoles(String roleName) {
        TypedQuery<Role> query = em.createQuery("SELECT r from Role r WHERE r.role = :roleName", Role.class);
        query.setParameter("roleName", roleName);
        return new HashSet<>(query.getResultList());
    }
}
