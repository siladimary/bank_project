package ru.siladimary.BankProject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.siladimary.BankProject.models.Account;
import ru.siladimary.BankProject.models.Transaction;
import ru.siladimary.BankProject.models.TransactionAction;
import ru.siladimary.BankProject.repositories.AccountsRepository;
import ru.siladimary.BankProject.repositories.PeopleRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional(readOnly = true)
public class AccountsService {
    private final AccountsRepository accountsRepository;

    @Autowired
    public AccountsService(AccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }

    public static Integer generateAccountNumber() {
        Random random = new Random();
        return 10000000 + random.nextInt(90000000);
    }

    public Optional<Account> findByAccountNumber(Integer number) {
        return accountsRepository.findByAccountNumber(number);
    }

    @Transactional
    public void depositAccount(Account account, BigDecimal amount) {
        checkAmount(amount);

        account.setBalance(account.getBalance().add(amount));

        Transaction transaction = new Transaction(TransactionAction.DEPOSIT, amount, account);

        account.getTransactions().add(transaction);
        transaction.setAccountNumber(account);
    }

    private void checkAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Сумма должна быть положительной");
    }

    //TODO методы для пополнения, снятия и перевода
}
