package com.springbootcamp.ecommerceapp.dtos;

import com.springbootcamp.ecommerceapp.validators.PasswordMatches;
import com.springbootcamp.ecommerceapp.validators.ValidEmail;
import com.springbootcamp.ecommerceapp.validators.ValidPassword;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@PasswordMatches
public class UserDto {

    @NotNull
    @NotEmpty
    @ValidEmail
    private String email;

    @NotNull
    @NotEmpty
    @ValidPassword
    private String password;

    @NotNull
    @NotEmpty
    private String confirmPassword;

    public UserDto() {
    }

    public UserDto(@NotNull @NotEmpty String email, @NotNull @NotEmpty String password, @NotNull @NotEmpty String confirmPassword) {
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
