package com.springbootcamp.ecommerceapp.dtos;

import com.springbootcamp.ecommerceapp.validators.PasswordMatches;
import com.springbootcamp.ecommerceapp.validators.ValidPassword;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@PasswordMatches
public class ForgotPassword {

    @NotNull
    @NotEmpty
    @ValidPassword
    String password;

    @NotNull
    @NotEmpty
    String confirmPassword;

    public ForgotPassword(@NotNull @NotEmpty String password, @NotNull @NotEmpty String confirmPassword) {
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
}
