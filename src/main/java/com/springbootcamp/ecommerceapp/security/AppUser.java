package com.springbootcamp.ecommerceapp.security;

import com.springbootcamp.ecommerceapp.entities.Address;
import com.springbootcamp.ecommerceapp.entities.Role;
import com.springbootcamp.ecommerceapp.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class AppUser implements UserDetails {

    private String username;
    private String password;

    private boolean isDeleted;
    private boolean isActive;
    private boolean isExpired;
    private boolean isLocked;

    private Set<Role> roles;

    public AppUser() {
    }

    public AppUser(User user){
        this.username = user.getEmail();
        this.password = user.getPassword();
        this.isActive = user.isActive();
        this.isDeleted = user.isDeleted();
        this.isExpired = user.isExpired();
        this.isLocked = user.isLocked();

        this.roles = new HashSet<>(user.getRoles());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !isExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}