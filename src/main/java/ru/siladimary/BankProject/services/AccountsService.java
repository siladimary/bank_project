package ru.siladimary.BankProject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.siladimary.BankProject.models.Account;
import ru.siladimary.BankProject.repositories.AccountsRepository;
import ru.siladimary.BankProject.repositories.PeopleRepository;

import java.math.BigDecimal;
import java.util.Random;

@Service
@Transactional(readOnly = true)
public class AccountsService {
    private final AccountsRepository accountsRepository;
    private final PeopleRepository peopleRepository;

    @Autowired
    public AccountsService(AccountsRepository accountsRepository, PeopleRepository peopleRepository) {
        this.accountsRepository = accountsRepository;
        this.peopleRepository = peopleRepository;
    }

    public static Integer generateAccountNumber(){
        Random random = new Random();
        return 10000000 + random.nextInt(90000000);
    }

    //TODO методы для пополнения, снятия и перевода
}
