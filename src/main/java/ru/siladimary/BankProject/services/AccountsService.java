package ru.siladimary.BankProject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.siladimary.BankProject.models.Account;
import ru.siladimary.BankProject.models.Transaction;
import ru.siladimary.BankProject.models.TransactionAction;
import ru.siladimary.BankProject.repositories.AccountsRepository;

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
    public void deposit(Account account, BigDecimal amount) {
        checkAmount(amount);

        account.setBalance(account.getBalance().add(amount));

        Transaction transaction = new Transaction(TransactionAction.DEPOSIT, amount, account);

        account.getTransactions().add(transaction);
        transaction.setAccountNumber(account);
    }

    @Transactional
    public void withdraw(Account account, BigDecimal amount){
        checkAmount(amount);
        checkWithdraw(account, amount);

        account.setBalance(account.getBalance().subtract(amount));

        Transaction transaction = new Transaction(TransactionAction.WITHDRAW, amount, account);

        account.getTransactions().add(transaction);
        transaction.setAccountNumber(account);
    }

    private void checkAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Сумма должна быть положительной");
    }

    private void checkWithdraw(Account account, BigDecimal amount){
        BigDecimal newBalance = account.getBalance().subtract(amount);

        if(newBalance.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("Недостаточно средств на счете");
        }
    }

    //TODO методы для пополнения, снятия и перевода
}
