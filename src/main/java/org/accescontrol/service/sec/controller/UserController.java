package org.accescontrol.service.sec.controller;


import org.accescontrol.service.sec.entities.AppUser;
import org.accescontrol.service.sec.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    AccountService accountService;

    @GetMapping("/users")
    public List<AppUser> getAllUser(){
        return accountService.listUsers();
    }

    @PostMapping("/users")
    public AppUser createUser(@RequestBody AppUser user){
        AppUser appUser = accountService.addNewUser(user);
        return appUser;
    }
}
