package ru.siladimary.BankProject.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationDTO {

    @NotEmpty(message = "Логин не должен быть пустым")
    private String username;

    @NotEmpty(message = "Пароль не должен быть пустым")
    @Size(min = 4, max = 4, message = "Пароль должен состоять из четырех символов")
    private String password;
}
