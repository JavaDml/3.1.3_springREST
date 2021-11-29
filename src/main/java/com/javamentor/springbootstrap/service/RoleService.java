package com.javamentor.springbootstrap.service;

import com.javamentor.springbootstrap.model.Role;

import java.util.Set;

public interface RoleService {
    Set<Role> getRoles();
    Set<Role> getRoles(String roleName);
}
