package com.javamentor.springbootstrap.dao;

import com.javamentor.springbootstrap.model.Role;

import java.util.Set;

public interface RoleDao {
    Set<Role> getRoles();
    Set<Role> getRoles(String roleName);
}
