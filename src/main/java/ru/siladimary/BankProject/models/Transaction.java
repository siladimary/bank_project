package ru.siladimary.BankProject.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Transaction")
public class Transaction {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Действие не должно быть пустым")
    @Column(name = "transaction_action")
    private TransactionAction transactionAction;

    @NotNull(message = "Укажите сумму")
    @Column(name = "amount")
    private BigDecimal amount;

    @NotNull(message = "Должно быть указано точное время")
    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "account_number", referencedColumnName = "account_number")
    @NotNull(message = "Номер счета должен быть заполнен")
    private Account accountNumber;

    public Transaction(TransactionAction transactionAction, BigDecimal amount, Account accountNumber) {
        this.transactionAction = transactionAction;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.accountNumber = accountNumber;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionAction=" + transactionAction +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                '}';
    }
}
