package com.springbootcamp.ecommerceapp.validators;

import com.springbootcamp.ecommerceapp.dtos.CustomerRegistrationDto;
import com.springbootcamp.ecommerceapp.dtos.ForgotPasswordDto;
import com.springbootcamp.ecommerceapp.dtos.SellerRegistrationDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator
        implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){

        if(obj instanceof SellerRegistrationDto){
            SellerRegistrationDto seller = (SellerRegistrationDto) obj;
            return seller.getPassword().equals(seller.getConfirmPassword());
        }
        else if(obj instanceof ForgotPasswordDto){
            ForgotPasswordDto passwords = (ForgotPasswordDto) obj;
            return passwords.getPassword().equals(passwords.getConfirmPassword());
        }

        CustomerRegistrationDto customer = (CustomerRegistrationDto) obj;
        return customer.getPassword().equals(customer.getConfirmPassword());
    }
}
