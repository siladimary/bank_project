package ru.siladimary.BankProject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.siladimary.BankProject.dto.TransferRequest;
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
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountsService(AccountsRepository accountsRepository, PasswordEncoder passwordEncoder) {
        this.accountsRepository = accountsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Integer generateUniqueAccountNumber(){
        Integer accountNumber;
        do {
            accountNumber = generateNumber();
        } while (accountsRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }

    public Optional<Account> findByAccountNumber(Integer number) {
        return accountsRepository.findByAccountNumber(number);
    }

    @Transactional
    public void deposit(Account account, BigDecimal amount) {
        checkAmount(amount);

        account.setBalance(account.getBalance().add(amount));

        createTransaction(TransactionAction.DEPOSIT, account, amount);
    }

    @Transactional
    public void withdraw(Account account, BigDecimal amount) {
        checkAmount(amount);
        checkWithdraw(account, amount);

        account.setBalance(account.getBalance().subtract(amount));

        createTransaction(TransactionAction.WITHDRAW, account, amount);
    }


    @Transactional
    public void transfer(Account senderAccount, TransferRequest transferRequest) {
        Optional<Account> recipientAccount = findByAccountNumber(transferRequest.getRecipientAccountNumber());
        if (recipientAccount.isEmpty())
            throw new IllegalArgumentException("Счет получателя не найден");

        if (senderAccount.getAccountNumber().equals(transferRequest.getRecipientAccountNumber()))
            throw new IllegalArgumentException("Вы ввели свой номер счета");

        if (!checkPassword(senderAccount, transferRequest.getPassword()))
            throw new IllegalArgumentException("Неверный пароль");

        checkAmount(transferRequest.getAmount());
        checkWithdraw(senderAccount, transferRequest.getAmount());

        senderAccount.setBalance(senderAccount.getBalance().subtract(transferRequest.getAmount()));
        createTransaction(TransactionAction.TRANSFER, senderAccount, transferRequest.getAmount());

        deposit(recipientAccount.get(), transferRequest.getAmount());
    }


    private Integer generateNumber() {
        return 10000000 + new Random().nextInt(90000000);
    }

    private void createTransaction(TransactionAction action, Account account, BigDecimal amount) {
        Transaction transaction = new Transaction(action, amount, account);

        account.getTransactions().add(transaction);
        transaction.setAccountNumber(account);
    }

    private void checkAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Сумма должна быть положительной");
    }

    private void checkWithdraw(Account account, BigDecimal amount) {
        BigDecimal newBalance = account.getBalance().subtract(amount);

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Недостаточно средств на счете");
        }
    }

    private boolean checkPassword(Account account, String password) {
        if (account == null || password == null)
            return false;

        return passwordEncoder.matches(password, account.getUsername().getPassword());
    }
}
