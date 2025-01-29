package com.syslogin.utils.mapper;

import com.syslogin.model.User;
import com.syslogin.presenter.form.UserForm;
import com.syslogin.presenter.dto.UserDTO;

public class UserDTOMapper {

    public static UserDTO map(final User user){
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .lastLogin(user.getLastLogin())
                .createdOn(user.getCreatedOn())
                .build();
    }

    public static User map(final UserForm formDTO){
        return User.builder()
                .email(formDTO.getEmail())
                .password(formDTO.getPassword())
                .telefone(formDTO.getTelefone())
                .username(formDTO.getUsername())
                .build();
    }

}
