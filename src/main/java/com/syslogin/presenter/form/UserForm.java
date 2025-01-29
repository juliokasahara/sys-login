package com.syslogin.presenter.form;

import com.syslogin.utils.validation.PasswordMatches;
import com.syslogin.utils.validation.UniqueEmail;
import com.syslogin.utils.validation.ValidEmail;
import com.syslogin.utils.validation.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@PasswordMatches
public class UserForm {

    @NotEmpty
    @Size(max = 45)
    private String username;

    @ValidEmail
    @UniqueEmail(message = "{user.already.exist}")
    private String email;

    @Column(nullable = false)
    private String telefone;

    @ValidPassword
    private String password;

    private String matchingPassword;
}


