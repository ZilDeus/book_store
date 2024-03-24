package com.zildeus.book_store.config.user;


import java.util.List;

//TODO : create a separate enum for permissions,maybe
public enum UserRole {
    Reader,Moderator,Admin;
    static public List<String> getRolePermissions(UserRole role){
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
}
