package com.syslogin.controller;

import com.syslogin.presenter.ResponseHandler;
import com.syslogin.presenter.dto.UserDTO;
import com.syslogin.presenter.form.UserForm;
import com.syslogin.service.UserService;
import com.syslogin.utils.AppMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
