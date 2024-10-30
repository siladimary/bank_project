package ru.siladimary.BankProject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.siladimary.BankProject.models.Account;
import ru.siladimary.BankProject.models.Person;
import ru.siladimary.BankProject.repositories.AccountsRepository;
import ru.siladimary.BankProject.repositories.PeopleRepository;

import java.math.BigDecimal;

@Service
public class RegistrationService {
    private final PeopleRepository peopleRepository;
    private final AccountsRepository accountsRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(PeopleRepository peopleRepository, AccountsRepository accountsRepository, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.accountsRepository = accountsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(Person person){
        String encodedPassword = passwordEncoder.encode(person.getPassword());
        person.setPassword(encodedPassword);

        Account account = new Account(person);
        person.getAccounts().add(account);

        peopleRepository.save(person);

        accountsRepository.save(account); // можем попробовать потом убрать, тк cascade = CascadeType.ALL
    }
}
