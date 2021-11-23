package com.javamentor.springbootstrap.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.google.gson.GsonBuilder;
import com.javamentor.springbootstrap.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.javamentor.springbootstrap.model.Role;
import com.javamentor.springbootstrap.model.User;
import com.javamentor.springbootstrap.service.UserService;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Controller
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Autowired
    private OAuth20Service oAuth20Service;

    @Autowired
    private AuthenticationManager authenticationManager;


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

        return "redirect:"+oAuth20Service.getAuthorizationUrl();
    }

    @GetMapping("/auth")
    public String GetUsers1(@RequestParam(value = "code", required = false) String code, Model model) throws InterruptedException, ExecutionException, IOException {
        OAuth2AccessToken accessToken = oAuth20Service.getAccessToken(code);
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.googleapis.com/oauth2/v3/userinfo");
        oAuth20Service.signRequest(accessToken, request);
        Response response = oAuth20Service.execute(request);

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String,String>> typeRef = new TypeReference<>() {};
        HashMap<String,String> responseMap = mapper.readValue(response.getBody(), typeRef);

        User googleUser = userService.getUserByName(responseMap.get("email"));
        if(googleUser == null) {
            User newGoogleUser = new User();
            newGoogleUser.setName(responseMap.get("email"));
            newGoogleUser.setSurname(responseMap.get("name"));
            newGoogleUser.setAge(0);
            newGoogleUser.setPassword(responseMap.get("sub"));
            newGoogleUser.setRoles(roleService.getRoles("ROLE_ADMIN"));
            userService.addOrEditUser(newGoogleUser, true);
            googleUser = userService.getUserByName(newGoogleUser.getName());
        }
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(googleUser, responseMap.get("sub"), googleUser.getAuthorities());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/crud_user";
    }

    @ModelAttribute("Users")
    public List<User> ListUsers() {
        return userService.getUsers();
    }
}
