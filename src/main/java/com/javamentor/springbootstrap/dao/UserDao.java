package com.javamentor.springbootstrap.dao;

import com.javamentor.springbootstrap.model.Role;
import com.javamentor.springbootstrap.model.User;
import java.util.List;

public interface UserDao {
    List<User> getUsers();
    void addOrEditUser(User user);
    boolean delUser(User user);
    User getUser(Long id);
    User getUserByName(String s);
    void createDataTables();
    Role getRole(String roleName);
}

