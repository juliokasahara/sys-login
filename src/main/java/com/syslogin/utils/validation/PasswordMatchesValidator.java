package com.syslogin.utils.validation;

import com.syslogin.presenter.form.UserForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(final PasswordMatches constraintAnnotation) {
        //
    }

    @Override
    public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
        final UserForm form = (UserForm) obj;
        boolean passwordMatches = form.getPassword().equals(form.getMatchingPassword());
        if(passwordMatches){
            return true;
        }
        context.buildConstraintViolationWithTemplate("{user.password.dont.match}").addPropertyNode("matchingPassword").addConstraintViolation();
        return false;
    }

}