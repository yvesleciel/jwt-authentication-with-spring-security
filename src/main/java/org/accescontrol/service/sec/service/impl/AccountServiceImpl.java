package org.accescontrol.service.sec.service.impl;

import org.accescontrol.service.sec.entities.AppRole;
import org.accescontrol.service.sec.entities.AppUser;
import org.accescontrol.service.sec.repositories.AppRoleRepository;
import org.accescontrol.service.sec.repositories.AppUserRepository;
import org.accescontrol.service.sec.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {


    private AppUserRepository appUserRepository;

    private AppRoleRepository appRoleRepository;

    private PasswordEncoder passwordEncoder;

    public AccountServiceImpl(AppUserRepository appUserRepository, AppRoleRepository appRoleRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.appRoleRepository = appRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public AppUser addNewUser(AppUser user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return appUserRepository.save(user);
    }

    @Override
    public AppRole addNewRole(AppRole role) {
        return appRoleRepository.save(role);
    }

    @Override
    public void addNewRoleToUser(String username, String roleName) {
       AppUser user = appUserRepository.findByUsername(username);
       AppRole role = appRoleRepository.findByRoleName(roleName);
        user.getAppRoles().add(role);
    }

    @Override
    public AppUser loadUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    @Override
    public List<AppUser> listUsers() {
        System.out.println("**************** account service");
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        return appUserRepository.findAll();
    }
}
