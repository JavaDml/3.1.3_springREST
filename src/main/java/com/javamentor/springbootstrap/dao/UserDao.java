package com.javamentor.springbootstrap.dao;

import com.javamentor.springbootstrap.model.User;

import java.util.List;

public interface UserDao {
    List<User> getUsers();
    void addOrEditUser(User myUser);
    boolean delUser(User myUser);
    User getUser(Long id);
    User getUserByName(String s);
    void createDataTables();
}

