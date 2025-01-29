package com.syslogin.service;

import com.syslogin.model.User;
import com.syslogin.enumeration.UserRolesEnum;
import com.syslogin.model.Role;
import com.syslogin.presenter.dto.LoginDTO;
import com.syslogin.presenter.dto.UserDTO;
import com.syslogin.presenter.form.LoginForm;
import com.syslogin.presenter.form.UserForm;
import com.syslogin.repository.RoleRepository;
import com.syslogin.repository.UserRepository;
import com.syslogin.utils.mapper.UserDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AuthService authService;

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

}
