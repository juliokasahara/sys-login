package com.syslogin.service;

import com.syslogin.presenter.dto.LoginDTO;
import com.syslogin.presenter.form.LoginForm;
import org.springframework.security.authentication.BadCredentialsException;

public interface AuthService {

    LoginDTO login(LoginForm form) throws BadCredentialsException;

}
