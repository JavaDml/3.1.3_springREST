package com.javamentor.springbootstrap.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import com.javamentor.springbootstrap.model.User;
import com.javamentor.springbootstrap.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // form 'user_panel'
    @GetMapping("/show_my_user")
    public String GetUsers() {
        return "/show_my_user.html";
    }

    @ModelAttribute("User")
    public User getUser(@AuthenticationPrincipal User user) {
        return user;
    }

}
