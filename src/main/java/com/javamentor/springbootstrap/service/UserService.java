package com.javamentor.springbootstrap.service;


import com.javamentor.springbootstrap.model.User;

import java.util.List;

public interface UserService {
    List<User> getUsers();
    boolean delUser(User myUser);
    void addOrEditUser(User myUser, boolean encPass);
    User getUser(Long id);
    User getUserByName(String name);
    void createDataTables();
}
