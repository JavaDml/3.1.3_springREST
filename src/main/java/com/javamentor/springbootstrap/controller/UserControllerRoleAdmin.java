package com.javamentor.springbootstrap.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.javamentor.springbootstrap.model.Role;
import com.javamentor.springbootstrap.service.RoleService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.javamentor.springbootstrap.model.User;
import com.javamentor.springbootstrap.service.UserService;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Controller
public class UserControllerRoleAdmin {

    private final UserService userService;
    private final RoleService roleService;
    private final OAuth20Service oAuth20Service;

    public UserControllerRoleAdmin(UserService userService, RoleService roleService, OAuth20Service oAuth20Service) {
        this.userService = userService;
        this.roleService = roleService;
        this.oAuth20Service = oAuth20Service;
    }

    // form 'admin_panel'
    @GetMapping("/crud_user")
    public String getUsers(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        model.addAttribute("CurrentUser", user.getName());
        model.addAttribute("CurrentRoles", user.getRolesToString());
        return "/crud_user";
    }

    // login
    @GetMapping("/login")
    public String getLogin() {
        return "/login";
    }

    @GetMapping("/")
    public String goLogin() {
        return "/login";
    }

    @GetMapping("/login/google")
    public  String redirectGoogle() {

        return "redirect:"+oAuth20Service.getAuthorizationUrl();
    }

    @GetMapping("/auth")
    public String getGoogleUser(Model model, @RequestParam(value = "code", required = false) String code) throws InterruptedException
            ,ExecutionException, IOException {
        OAuth2AccessToken accessToken = oAuth20Service.getAccessToken(code);
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.googleapis.com/oauth2/v3/userinfo");
        oAuth20Service.signRequest(accessToken, request);
        Response response = oAuth20Service.execute(request);

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String,String>> typeRef = new TypeReference<>() {};
        HashMap<String,String> responseMap = mapper.readValue(response.getBody(), typeRef);

        Set<Role> roles = roleService.getRoles();
        UsernamePasswordAuthenticationToken usernamePassAuthToken = new UsernamePasswordAuthenticationToken(responseMap.get("email")
                ,responseMap.get("sub"), roles);
        SecurityContextHolder.getContext().setAuthentication(usernamePassAuthToken);

        StringBuilder stringRoles = new StringBuilder();
        for (Role role : roles
        ) {
            stringRoles.append(role.getRole()).append(" ");
        }
        model.addAttribute("CurrentUser", responseMap.get("email"));
        model.addAttribute("CurrentRoles", stringRoles);


        return "/crud_user";
    }

    @ModelAttribute("Users")
    public List<User> listUsers() {
        return userService.getUsers();
    }
}
