package ru.siladimary.BankProject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.siladimary.BankProject.models.Account;
import ru.siladimary.BankProject.models.Person;
import ru.siladimary.BankProject.repositories.PeopleRepository;


@Service
public class RegistrationService {
    private final PeopleRepository peopleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountsService accountsService;

    @Autowired
    public RegistrationService(PeopleRepository peopleRepository, PasswordEncoder passwordEncoder, AccountsService accountsService) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountsService = accountsService;
    }

    @Transactional
    public void register(Person person) {
        String encodedPassword = passwordEncoder.encode(person.getPassword());
        person.setPassword(encodedPassword);

        Account account = new Account(accountsService.generateUniqueAccountNumber(), person);

        person.getAccounts().add(account);
        account.setUsername(person);

        peopleRepository.save(person);
    }
}
