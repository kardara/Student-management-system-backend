package com.kardara.studentManagement.model;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class StaffPrincipal implements UserDetails {

    private Staff staff;

    public StaffPrincipal (Staff st){
        this.staff = st;
    }
    
    public String getUsername(){
        return staff.getEmail();
    }

    public String getPassword(){
        return null;
    }

    public Collection<? extends GrantedAuthority> getAuthorities(){
        return Collections.singleton(new SimpleGrantedAuthority(staff.getRole().toString()));
    }
}
