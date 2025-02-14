package com.syslogin.service;

import com.syslogin.enumeration.UserRolesEnum;
import com.syslogin.model.Role;
import com.syslogin.model.User;
import com.syslogin.presenter.dto.LoginDTO;
import com.syslogin.presenter.dto.UserDTO;
import com.syslogin.presenter.form.LoginForm;
import com.syslogin.presenter.form.NewPasswordForm;
import com.syslogin.presenter.form.UserForm;
import com.syslogin.repository.RoleRepository;
import com.syslogin.repository.UserRepository;
import com.syslogin.utils.mapper.UserDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AuthService authService;
    private final EmailServiceImpl emailService;

    @Override
    public UserDTO registerNewUserAccount(UserForm form) {
        LoginForm loginForm = new LoginForm(form.getEmail(), form.getPassword());
        form.setPassword(passwordEncoder.encode(form.getPassword()));
        User user = UserDTOMapper.map(form);
        addRoleToUser(user,UserRolesEnum.ROLE_USER.getRoleId());
        var savedUser = userRepository.save(user);
        log.info("Created user {}", form.getUsername());

        LoginDTO loginDTO = authService.login(loginForm);
        UserDTO mappedUser = UserDTOMapper.map(savedUser);
        mappedUser.setAccessToken(loginDTO.getAccessToken());
        return mappedUser;
    }

    @Override
    public void registerNewPassword(NewPasswordForm userForm) {
        User user = userRepository.getByResetPasswordToken(userForm.getToken()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuário não encontrado.",null));

        user.setPassword(userForm.getPassword());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(userForm.getPassword());
        user.setPassword(encodedPassword);

        user.setResetPasswordToken(null);
        userRepository.save(user);
    }


    public void updateResetPasswordToken(String cpfEmail,String path) throws UnsupportedEncodingException {
        try {
            User user = userRepository.findByEmail(cpfEmail).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuário não encontrado." + cpfEmail));
            user.setResetPasswordToken(RandomString.make(40));

            String resetPasswordLink = path + "/new-password?token=" + user.getResetPasswordToken();
            emailService.sendEmail(user.getEmail(), resetPasswordLink);

            userRepository.save(user);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
            new ResponseStatusException(HttpStatus.NOT_FOUND,"Erro ao enviar o E-mail");
            throw e;
        } catch (javax.mail.MessagingException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void addRoleToUser(User user, Long roleId) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new UsernameNotFoundException("Not found role!"));
        if(CollectionUtils.isEmpty(user.getRoles())){
            user.setRoles(new ArrayList<>());
        }
        user.getRoles().add(role);
        role.setUsers(List.of(user));
        log.info("Granted role {} to user {}", roleId, user.getUsername());
    }

    public boolean emailExists(final String email){
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByEmail(username).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuário não encontrado: " + username));
        return UserDTO.builder().username(user.getUsername()).email(user.getEmail()).build();
    }

}
