package com.syslogin.service;

import com.syslogin.presenter.dto.UserDTO;
import com.syslogin.presenter.form.NewPasswordForm;
import com.syslogin.presenter.form.UserForm;

import java.io.UnsupportedEncodingException;

public interface UserService {
    //    void addRoleToUser(Long userId, Long roleId);
    void updateResetPasswordToken(String cpfEmail,String path) throws UnsupportedEncodingException;
    UserDTO registerNewUserAccount(UserForm userForm);
    void registerNewPassword(NewPasswordForm userForm);
    boolean emailExists(final String email);
    UserDTO getUserByUsername(String username);
}
