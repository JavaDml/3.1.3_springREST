package com.javamentor.springbootstrap.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.javamentor.springbootstrap.dao.UserDao;
import com.javamentor.springbootstrap.model.Role;
import com.javamentor.springbootstrap.model.User;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean delUser(Long id) {
        return userDao.delUser(id);
    }

    @Override
    public User getUser(Long id) {
        return userDao.getUser(id);
    }

    @Override
    public void addOrEditUser(User user, boolean encPass) {
        if(encPass == true) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userDao.addOrEditUser(user);
    }

    @Override
    public List<User> getUsers() {
        return userDao.getUsers();
    }

    @Override
    public Role getRole(String roleName) {
        return userDao.getRole(roleName);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userDao.getUserByName(s);
    }

    public void createDataTables() {
        userDao.createDataTables();
    }


}
