package com.zildeus.book_store.service;

import com.zildeus.book_store.config.user.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserRolesService {
    public List<String> getRolePermissions(UserRole role){
        return switch (role)
        {
            case Reader -> List.of("READ","VIEW","REVIEW","COMMENT","REFRESH_TOKEN","POST");
            case Moderator -> List.of("DELETE","EDIT","REPORT");
            case Admin -> List.of("ADD_BALANCE","BAN","ELEVATE_ROLE");
            //combine roles!
            /*
            case Admin -> {
                List<String> list = new ArrayList<>(List.of("BAN", "ELEVATE_ROLE"));
                //make sure you don't accidentally create an infinite loop
                list.addAll(getRolePermissions(UserRole.Moderator));
                yield list;
            }
             */
        };
    }
    public String GetUserPermissionsFromAuthentication(Authentication authentication)
    {
        return GetUserPermissionFromRoles(GetUserRoles(authentication));
    }
    public List<UserRole> GetUserRoles(Authentication authentication){
        return authentication.getAuthorities().stream()
                .map(auth->UserRole.valueOf(auth.toString()))
                .toList();
    }
    public String GetUserPermissionFromRoles(List<UserRole> roles){
        Set<String> perms = new HashSet<>();
        roles.forEach(role->perms.addAll(getRolePermissions(role)));
        return String.join(" ", perms);
    }
}
