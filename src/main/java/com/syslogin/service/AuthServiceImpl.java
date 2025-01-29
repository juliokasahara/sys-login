package com.syslogin.service;

import com.syslogin.model.User;
import com.syslogin.presenter.dto.LoginDTO;
import com.syslogin.presenter.form.LoginForm;
import com.syslogin.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService{
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public LoginDTO login(LoginForm form) throws BadCredentialsException {

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(form.getEmail(), form.getPassword()));
        User loggedUser = (User) authenticate.getPrincipal();

        String accessToken = jwtTokenUtil.generateAccessToken(loggedUser,authenticate);
        LoginDTO loginData = new LoginDTO(loggedUser.getEmail(), accessToken);
        log.info("User {} has been logged in", loggedUser.getEmail());
        return loginData;
    }

}
