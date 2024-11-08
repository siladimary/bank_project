package ru.siladimary.BankProject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Person")
public class Person {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Поле с именем не должно быть пустым")
    @Pattern(regexp = "[А-Я][а-яё]+")
    @Column(name = "first_name")
    private String firstName;

    @NotNull(message = "Поле с фамилией не должно быть пустым")
    @Pattern(regexp = "[А-Я][а-яё]+")
    @Column(name = "last_name")
    private String lastName;

    @NotNull(message = "Поле с возрастом не должно быть пустым")
    @Min(value = 14, message = "Вам должно быть больше 14 лет")
    @Max(value = 99, message = "Вам не может быть больше 99 лет")
    @Column(name = "age")
    private Integer age;

    @NotNull(message = "Логин не может быть пустым")
    @Column(name = "username")
    private String username;

    @NotNull(message = "Пароль не может быть пустым")
    @Column(name = "password")
    private String password;

    @NotNull(message = "Баланс не может быть null")
    @Column(name = "total_balance")
    private BigDecimal totalBalance;

    @OneToMany(mappedBy = "username", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Account> accounts = new ArrayList<>();

    public Person(String firstName, String lastName, Integer age, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.username = username;
        this.password = password;
        this.totalBalance = BigDecimal.ZERO;
        this.accounts = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Person{" +
                "username='" + username + '\'' +
                '}';
    }
}
