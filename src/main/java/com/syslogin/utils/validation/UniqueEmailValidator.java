package com.syslogin.utils.validation;

import com.syslogin.service.UserService;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    private final UserService userService;

    @Override
    public boolean isValid(final String email, final ConstraintValidatorContext context) {
        return !userService.emailExists(email);
    }

}