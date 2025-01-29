package com.syslogin.service;

import com.syslogin.presenter.dto.UserDTO;
import com.syslogin.presenter.form.UserForm;

public interface UserService {
    UserDTO registerNewUserAccount(UserForm userForm);
//    void addRoleToUser(Long userId, Long roleId);
    boolean emailExists(final String email);
}
