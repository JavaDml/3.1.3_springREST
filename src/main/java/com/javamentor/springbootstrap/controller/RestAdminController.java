package com.javamentor.springbootstrap.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.javamentor.springbootstrap.model.Role;
import com.javamentor.springbootstrap.model.User;
import com.javamentor.springbootstrap.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/rest")
public class RestAdminController {

    private final UserService userService;

    public RestAdminController(UserService userService) {
        this.userService = userService;
    }

    // Add or Edit User
    @PostMapping("/addOrEdit_user")
    public ResponseEntity<?> addUser(@RequestBody User user
            , @RequestParam(value = "originalPass", required = false) String originalPass
            , @RequestParam(value = "checkBoxRoles", required = false) String[] rolesSelector) {
        if((user.getName() != null) & (user.getPassword() != null)) {
            Set<Role> roles = new HashSet<>();
            for(String role : rolesSelector) {
                roles.add(new Role(role));
            }
            if(!roles.isEmpty()){
                user.setRoles(roles);
                if( (user.getId() == null) || !(user.getPassword().equals(originalPass)) ){
                    userService.addOrEditUser(user, true);
                } else {
                    userService.addOrEditUser(user, false);
                }
            }
        }
        return user.getId() != null
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.CREATED);
    }

    // Get User (for upd or del user)
    @GetMapping("/get_user/{id}")
    public ResponseEntity<User> GetUser(@PathVariable("id") Long id) {
        final User user = userService.getUser(id);
        return user != null
                ? new ResponseEntity<>(user, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Delete User
    @PostMapping("/del_user/{id}")
    public ResponseEntity<?> DeleteUser(@PathVariable("id") Long id) {
        final boolean deleted = userService.delUser(id);
        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @GetMapping("/getAllUsers")
    public  ResponseEntity<List<User>> getAllUsers() {
        final List<User> users = userService.getUsers();

        return users != null &&  !users.isEmpty()
                ? new ResponseEntity<>(users, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    /*@ModelAttribute("Users")
    public List<User> ListUsers() {
        return userService.getUsers();
    }

    @ModelAttribute("User")
    public User getNewUser() {
        return new User();
    }*/
}
