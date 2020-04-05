package com.springbootcamp.ecommerceapp.dtos;

import com.springbootcamp.ecommerceapp.validators.PasswordMatches;
import com.springbootcamp.ecommerceapp.validators.ValidPassword;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@PasswordMatches
public class ForgotPassword {

    @NotNull
    @NotEmpty
    @ValidPassword
    String password;

    @NotNull
    @NotEmpty
    String confirmPassword;

    public ForgotPassword() {
    }

    public ForgotPassword(@NotNull @NotEmpty String password, @NotNull @NotEmpty String confirmPassword) {
        this.password = password;
        this.confirmPassword = confirmPassword;
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
