package com.library.mapper;

import com.library.model.ERole;
import com.library.model.Role;
import com.library.model.User;
import com.library.dto.UserDto;
import com.library.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder encoder;
    private final RoleRepository roleRepository;

    public User mapToUser(UserDto userDto) {
        Set<String> strRoles = userDto.getRole();
        Set<Role> roles = new HashSet<>();

        checkRolesPresent();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        return User.builder()
                .username(userDto.getUsername())
                .name(userDto.getName())
                .surname(userDto.getSurname())
                .email(userDto.getEmail())
                .password(encoder.encode(userDto.getPassword()))
                .roles(roles)
                .build();
    }

    public UserDto mapToUserDto(User user) {
        Set<String> roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());

        return new UserDto(
                user.getUsername(),
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                roles,
                user.getPassword(),
                user.getSignUpDate());
    }

    public List<UserDto> mapToUserDtoList(List<User> userList) {
            return userList.stream()
                    .map(this::mapToUserDto)
                    .collect(Collectors.toList());
    }

    private void checkRolesPresent() {

        if(!(roleRepository.findByName(ERole.ROLE_USER).isPresent())) {
            Role role = new Role(ERole.ROLE_USER);
            roleRepository.save(role);
        }
        if(!(roleRepository.findByName(ERole.ROLE_MODERATOR).isPresent())) {
            Role role = new Role(ERole.ROLE_MODERATOR);
            roleRepository.save(role);
        }
        if(!(roleRepository.findByName(ERole.ROLE_ADMIN).isPresent())) {
            Role role = new Role(ERole.ROLE_ADMIN);
            roleRepository.save(role);
        }
    }
}
