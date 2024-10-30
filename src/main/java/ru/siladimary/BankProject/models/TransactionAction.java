package ru.siladimary.BankProject.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransactionAction {
    DEPOSIT("Пополнение"), WITHDRAW("Снятие"), TRANSFER("Перевод");

    private final String translation;
}
