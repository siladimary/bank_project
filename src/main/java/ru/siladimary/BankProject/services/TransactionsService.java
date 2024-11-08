package ru.siladimary.BankProject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.siladimary.BankProject.models.Account;
import ru.siladimary.BankProject.models.Transaction;
import ru.siladimary.BankProject.repositories.TransactionsRepository;


@Service
@Transactional(readOnly = true)
public class TransactionsService {
    private final TransactionsRepository transactionsRepository;
    private final AccountsService accountsService;

    @Autowired
    public TransactionsService(TransactionsRepository transactionsRepository, AccountsService accountsService) {
        this.transactionsRepository = transactionsRepository;
        this.accountsService = accountsService;
    }

    public Page<Transaction> findTransactions(Integer accountNumber, Pageable pageable){
        Account account = accountsService.checkAccount(accountNumber);
        return transactionsRepository.findByAccountNumber(account, pageable);
    }
}
