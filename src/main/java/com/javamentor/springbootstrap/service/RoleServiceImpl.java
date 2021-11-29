package com.javamentor.springbootstrap.service;


import com.javamentor.springbootstrap.dao.RoleDao;
import com.javamentor.springbootstrap.model.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    RoleDao roleDao;

    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }



    @Transactional(readOnly = true)
    @Override
    public Set<Role> getRoles() {
        return roleDao.getRoles();
    }

    @Transactional(readOnly = true)
    @Override
    public Set<Role> getRoles(String roleName) {
        return roleDao.getRoles(roleName);
    }
}
