package com.syslogin.presenter.form;

import com.syslogin.utils.validation.PasswordMatches;
import com.syslogin.utils.validation.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@PasswordMatches
public class NewPasswordForm {

    private String password;

    private String matchingPassword;

    private String token;
}


