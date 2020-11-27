package com.library.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(min = 3, max = 20)
    private String name;

    @NotBlank
    @Size(min = 3, max = 20)
    private String surname;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private Set<String> role;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    private LocalDateTime signUpDate;

    public UserDto(@NotBlank @Size(min = 3, max = 20) String username,
                   @NotBlank @Size(min = 3, max = 20) String name,
                   @NotBlank @Size(min = 3, max = 20) String surname,
                   @NotBlank @Size(max = 50)
                   @Email String email, Set<String> role,
                   @NotBlank @Size(min = 6, max = 40) String password) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.role = role;
        this.password = password;
    }
}
