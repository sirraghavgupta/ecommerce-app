package com.springbootcamp.ecommerceapp.validators;

import com.springbootcamp.ecommerceapp.dtos.CustomerDto;
import com.springbootcamp.ecommerceapp.dtos.UserDto;
import com.springbootcamp.ecommerceapp.entities.Customer;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator
        implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }
    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){
        UserDto user = (UserDto) obj;
        return user.getPassword().equals(user.getConfirmPassword());
    }
}
