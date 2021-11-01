package com.javamentor.springbootstrap.service;


import com.javamentor.springbootstrap.model.Role;
import com.javamentor.springbootstrap.model.User;
import java.util.List;

public interface UserService {
    List<User> getUsers();
    boolean delUser(Long id);
    void addOrEditUser(User user, boolean encPass);
    User getUser(Long id);
    void createDataTables();
    Role getRole(String roleName);
}
