package com.springbootcamp.ecommerceapp.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class AppUser implements UserDetails {

    private Long id;

    private String username;
    private String firstName;
    private String middleName;
    private String lastName;
    private String password;

    private boolean isDeleted;
    private boolean isActive;
    private boolean isExpired;
    private boolean isLocked;

    private Set<Role> roles;

    private Set<Address> addresses;

    public AppUser() {
    }

    public AppUser(User user){
        this.id = user.getId();
        this.username = user.getEmail();
        this.firstName = user.getFirstName();
        this.middleName = user.getMiddleName();
        this.lastName = user.getLastName();
        this.password = user.getPassword();
        this.isActive = user.isActive();
        this.isDeleted = user.isDeleted();
        this.isExpired = user.isExpired();
        this.isLocked = user.isLocked();

        this.roles = new HashSet<>(user.getRoles());

        this.addresses = new HashSet<Address>(user.getAddresses());
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