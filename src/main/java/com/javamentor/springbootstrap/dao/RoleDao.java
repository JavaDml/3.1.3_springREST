package com.javamentor.springbootstrap.dao;

import com.javamentor.springbootstrap.model.Role;

import java.util.Collection;

public interface RoleDao {
    Collection<Role> getRoles();
    Collection<Role> getRoles(String roleName);
}
