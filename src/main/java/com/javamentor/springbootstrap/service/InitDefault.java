package com.javamentor.springbootstrap.service;


import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

@Component
public class InitDefault {

    private final UserService userService;

    public InitDefault(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void createDataTables() {
        userService.createDataTables();
    }

}
