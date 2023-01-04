package org.accescontrol.service;

import org.accescontrol.service.sec.entities.AppRole;
import org.accescontrol.service.sec.entities.AppUser;
import org.accescontrol.service.sec.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class AccescontroleApplication implements CommandLineRunner {

    @Autowired
    AccountService accountService;

    public static void main(String[] args) {
        SpringApplication.run(AccescontroleApplication.class, args);
    }


    @Override
    public void run(String... args){
        accountService.addNewRole(new AppRole(null, "ADMIN"));
        accountService.addNewRole(new AppRole(null, "USER"));
        accountService.addNewRole(new AppRole(null, "MANAGER"));

        accountService.addNewUser(new AppUser(null, "Yvan", "123",new ArrayList<>()));
        accountService.addNewUser(new AppUser(null, "Younes", "123",new ArrayList<>()));
        accountService.addNewUser(new AppUser(null, "Leciel", "123",new ArrayList<>()));

        accountService.addNewRoleToUser("Yvan","ADMIN");
        accountService.addNewRoleToUser("Younes","USER");
        accountService.addNewRoleToUser("Leciel","MANAGER");
    }
}
