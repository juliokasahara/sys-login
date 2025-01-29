package com.syslogin.controller;

import com.syslogin.presenter.ResponseHandler;
import com.syslogin.presenter.dto.LoginDTO;
import com.syslogin.presenter.form.LoginForm;
import com.syslogin.service.AuthService;
import com.syslogin.utils.AppMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AppMessage appMessage;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginDTO> login(@RequestBody @Valid LoginForm form) throws Exception {
        try {
            LoginDTO response = authService.login(form);
            return ResponseHandler.generateResponse(appMessage.getMessage("auth.login.success"), HttpStatus.OK, response);
        }catch (BadCredentialsException e){
            return ResponseHandler.generateResponse(appMessage.getMessage("auth.bad.credentials"), HttpStatus.BAD_REQUEST, null);
        }catch (Exception e){
            return ResponseHandler.generateResponse(appMessage.getMessage("internal.server.error"), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

}
