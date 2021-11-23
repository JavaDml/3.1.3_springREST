package com.javamentor.springbootstrap.service;


import com.javamentor.springbootstrap.dao.RoleDao;
import com.javamentor.springbootstrap.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleDao roleDao;

    @Override
    public Collection<Role> getRoles() {
        return roleDao.getRoles();
    }

    @Override
    public Collection<Role> getRoles(String roleName) {
        return roleDao.getRoles(roleName);
    }
}
