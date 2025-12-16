package com.kardara.studentManagement.model;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class TeacherPrincipal implements UserDetails {

     private Teacher teacher;
    
        public TeacherPrincipal (Teacher st){
            this.teacher = st;
        }
        
        public String getUsername(){
            return teacher.getEmail();
        }
    
        public String getPassword(){
            return null;
        }
    
        public Collection<? extends GrantedAuthority> getAuthorities(){
            return Collections.singleton(new SimpleGrantedAuthority(teacher.getRole().toString()));
        }
}
