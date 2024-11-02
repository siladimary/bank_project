package ru.siladimary.BankProject.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransferRequest {
    @NotNull(message = "Заполните номер счета для перевода (8 цифр)")
    private Integer recipientAccountNumber;
    @NotNull(message = "Введите сумму")
    private BigDecimal amount;
    @NotNull(message = "Введите пароль")
    @Size(min = 4, max = 4, message = "Пароль должен состоять из четырех символов")
    private String password;
}
