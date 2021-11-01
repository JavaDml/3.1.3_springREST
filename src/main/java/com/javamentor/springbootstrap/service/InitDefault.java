package com.javamentor.springbootstrap.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

@Component
public class InitDefault {

    @Autowired
    private UserService userService;

    @PostConstruct
    public void createDataTables() {
        userService.createDataTables();
    }

}
