package com.syslogin.controller;

import com.syslogin.presenter.ResponseHandler;
import com.syslogin.presenter.dto.UserDTO;
import com.syslogin.presenter.form.NewPasswordForm;
import com.syslogin.presenter.form.UserForm;
import com.syslogin.service.UserService;
import com.syslogin.utils.AppMessage;
import com.syslogin.utils.PathUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AppMessage appMessage;

    @PostMapping("/save")
    public ResponseEntity<UserDTO> saveUser(@RequestBody @Valid UserForm form){
        try {
            var userDTO = userService.registerNewUserAccount(form);

            return ResponseHandler.generateResponse(appMessage.getMessage("user.successfully.created"), HttpStatus.OK, userDTO);
        }catch (Exception e){
            return ResponseHandler.generateResponse(appMessage.getMessage("user.creation.error"), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PostMapping("/recover-password")
    public ResponseEntity<?> processForgotPassword(HttpServletRequest request, @RequestBody UserForm form) throws Exception {

        try {
            userService.updateResetPasswordToken(form.getEmail(), PathUtil.getFrontURL(request));
        }catch (ResponseStatusException e){
            return ResponseHandler.generateResponse("Usuário não encontrado.", HttpStatus.BAD_REQUEST, null);
        }catch (Exception e){
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY, null);
        }
        return ResponseHandler.generateResponse(appMessage.getMessage("success"), HttpStatus.OK, null);
    }

    @PostMapping("/new-password")
    public ResponseEntity<?> processResetPassword(@RequestBody NewPasswordForm form) {
        try {
            userService.registerNewPassword(form);
            return ResponseHandler.generateResponse(appMessage.getMessage("success"), HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(appMessage.getMessage("user.not.found"), HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getInfoMe(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            UserDTO userDTO = userService.getUserByUsername(userDetails.getUsername());

            return ResponseHandler.generateResponse(
                    appMessage.getMessage("success"),
                    HttpStatus.OK,
                    userDTO
            );
        } catch (Exception e) {
            return ResponseHandler.generateResponse(
                    appMessage.getMessage("user.fetch.error"),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null
            );
        }
    }

//    @PostMapping("/addRoleToUser")
//    @Secured({IRoles.ROLE_ADMIN})
//    public ResponseEntity<?> addRoleToUser(@RequestParam Long userId, @RequestParam Long roleId){
//        try{
//            userService.addRoleToUser(userId, roleId);
//            return ResponseHandler.generateResponse(appMessage.getMessage("permission.grant.success"), HttpStatus.OK, null);
//        }catch (DataIntegrityViolationException e){
//            return ResponseHandler.generateResponse(appMessage.getMessage("permission.grant.failed"), HttpStatus.BAD_REQUEST, null);
//        } catch (Exception e){
//            return ResponseHandler.generateResponse(appMessage.getMessage("internal.server.error"), HttpStatus.INTERNAL_SERVER_ERROR, null);
//        }
//    }

}
