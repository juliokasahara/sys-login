package com.syslogin.utils.validation;

import com.google.common.base.Joiner;
import lombok.SneakyThrows;
import org.passay.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(final ValidPassword arg0) {

    }

    @SneakyThrows
    @Override
    public boolean isValid(final String password, final ConstraintValidatorContext context) {
        Properties props = new Properties();
        props.load(new FileInputStream("src/main/resources/messages.properties"));
        MessageResolver resolver = new PropertiesMessageResolver(props);

        List<Rule> rules = Arrays.asList(
                new LengthRule(7, 30),
                new UppercaseCharacterRule(1),
                new DigitCharacterRule(1),
                new WhitespaceRule());

        final PasswordValidator validator = new PasswordValidator(resolver, rules);
        final RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(Joiner.on(",").join(validator.getMessages(result))).addConstraintViolation();
        return false;
    }

}