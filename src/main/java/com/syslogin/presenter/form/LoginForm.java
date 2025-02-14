package com.syslogin.presenter.form;

import com.syslogin.utils.validation.ValidEmail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginForm {
    @ValidEmail
    private String email;
    private String password;
}
