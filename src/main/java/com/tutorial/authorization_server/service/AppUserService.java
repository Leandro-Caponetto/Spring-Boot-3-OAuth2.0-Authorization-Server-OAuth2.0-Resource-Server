package com.tutorial.authorization_server.service;


import com.tutorial.authorization_server.dto.CreateAppUserDto;
import com.tutorial.authorization_server.dto.MessageDto;
import com.tutorial.authorization_server.entity.AppUser;
import com.tutorial.authorization_server.entity.Role;
import com.tutorial.authorization_server.enums.RoleName;
import com.tutorial.authorization_server.repository.AppUserRepository;
import com.tutorial.authorization_server.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final RoleRepository repository;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository appUserRepository, RoleRepository repository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public MessageDto createUser(CreateAppUserDto dto){
        AppUser appUser = AppUser.builder()
                .username(dto.username())
                .password(passwordEncoder.encode(dto.password()))
                .build();

        Set<Role> roles = new HashSet<>();
        dto.roles().forEach(r -> {
            Role role = repository.findByRole(RoleName.valueOf(r))
                    .orElseThrow(() -> new RuntimeException("role not found"));
            roles.add(role);
        });
        appUser.setRoles(roles);
        appUserRepository.save(appUser);
        return new MessageDto("user " + appUser.getUsername() + " saved" );
    }
}

