package com.javamentor.springbootstrap.service;

import com.javamentor.springbootstrap.model.Role;

import java.util.Collection;

public interface RoleService {
    Collection<Role> getRoles();
    Collection<Role> getRoles(String roleName);
}
