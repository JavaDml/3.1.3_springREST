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
//@RequestMapping
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    // form 'admin_panel'
    @GetMapping("/crud_user")
    public String GetUsers(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("CurrentUser", auth.getPrincipal());
        return "/crud_user";
    }

    // login
    @GetMapping("/login")
    public String GetLogin() {
        return "/login";
    }

    @GetMapping("/login/google")
    public  String GetGoogle() {

        return null;
    }

    @ModelAttribute("Users")
    public List<User> ListUsers() {
        return userService.getUsers();
    }
}
