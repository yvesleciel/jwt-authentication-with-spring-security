package org.accescontrol.service.sec.service;

import org.accescontrol.service.sec.entities.AppRole;
import org.accescontrol.service.sec.entities.AppUser;

import java.util.List;

public interface AccountService {
    public AppUser addNewUser(AppUser user);
    public AppRole addNewRole(AppRole role);
    public void addNewRoleToUser(String username, String roleName);
    public AppUser loadUserByUsername(String username);
    public List<AppUser> listUsers();
}
