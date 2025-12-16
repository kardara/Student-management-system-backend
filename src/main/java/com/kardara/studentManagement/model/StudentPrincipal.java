package com.kardara.studentManagement.model;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class StudentPrincipal implements UserDetails{

    private Student student;

    public StudentPrincipal(Student student){
        this.student = student;
    }

    public String getUsername(){
        return student.getEmail();
    }

    public String getPassword(){
        return null;
    }

    public Collection<? extends GrantedAuthority> getAuthorities(){
        return Collections.singleton(new SimpleGrantedAuthority("STUDENT"));
    }

    public boolean isAccountNonExpired() {
		return student.getStatus().toString().equals("GRADUATED");
	}

	
	public boolean isAccountNonLocked() {
		return student.getStatus().toString().equals("SUPENDED");
	}

    public boolean isEnabled() {
		return student.getStatus().toString().equals("EXPULSED");
	}

}
