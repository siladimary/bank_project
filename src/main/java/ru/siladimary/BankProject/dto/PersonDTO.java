package ru.siladimary.BankProject.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import ru.siladimary.BankProject.models.Account;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PersonDTO {

    @NotNull(message = "Поле с именем не должно быть пустым")
    @Pattern(regexp = "[А-Я][а-яё]+")
    private String firstName;

    @NotNull(message = "Поле с фамилией не должно быть пустым")
    @Pattern(regexp = "[А-Я][а-яё]+")
    private String lastName;

    @NotNull(message = "Поле с возрастом не должно быть пустым")
    @Min(value = 14, message = "Вам должно быть больше 14 лет")
    @Max(value = 99, message = "Вам не может быть больше 99 лет")
    private Integer age;

    @NotNull(message = "Логин не может быть пустым")
    private String username;

    @NotNull(message = "Пароль не может быть пустым")
    @Size(min = 4, max = 4, message = "Пароль должен состоять из четырех символов")
    private String password;

    @NotNull(message = "Подтвердите пароль")
    @Size(min = 4, max = 4, message = "Пароль должен состоять из четырех символов")
    private String confirmPassword;

    @NotNull(message = "Баланс не может быть null")
    private BigDecimal totalBalance;

    private List<Account> accounts;

    public PersonDTO(String firstName, String lastName, Integer age,
                     String username, String password, String confirmPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.totalBalance = BigDecimal.ZERO;
        this.accounts = new ArrayList<>();
    }
}
