package com.javamentor.springbootstrap.controller;

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

@Controller
@RequestMapping("/crud_user")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    // form 'admin_panel'
    @GetMapping
    public String GetUsers(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("CurrentUser", auth.getPrincipal());
        return "/crud_user";
    }

/*    // Add or Edit User
    @PostMapping("/addOrEdit_user")
    public String addUser(@ModelAttribute ("User") User user
            ,@RequestParam(value = "originalPass", required = false) String originalPass
            ,@RequestParam(value = "checkBoxRoles", required = false) String[] rolesSelector) {
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
        return "redirect:/admin/crud_user";
    }

    // Get User (for upd or del user)
    @GetMapping("/get_user/{id}")
    public User GetUser(@PathVariable("id") Long id) {
        return userService.getUser(id);
    }

    // Delete User
    @PostMapping("/del_user/{id}")
    public String DeleteUser(@PathVariable("id") Long id) {
        userService.delUser(id);
        return "redirect:/admin/crud_user";
    }
*/
    @ModelAttribute("Users")
    public List<User> ListUsers() {
        return userService.getUsers();
    }

    @ModelAttribute("User")
    public User getNewUser() {
        return new User();
    }
}
