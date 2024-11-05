package ru.siladimary.BankProject.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransactionAction {
    DEPOSIT("Пополнение"), WITHDRAW("Снятие"), TRANSFER_OUT("Перевод"),
    TRANSFER_IN("Входящий перевод");

    private final String translation;
}
