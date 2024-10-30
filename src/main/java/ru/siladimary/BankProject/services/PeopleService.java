package ru.siladimary.BankProject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.siladimary.BankProject.exceptions.PersonNotFoundException;
import ru.siladimary.BankProject.models.Account;
import ru.siladimary.BankProject.models.Person;
import ru.siladimary.BankProject.repositories.AccountsRepository;
import ru.siladimary.BankProject.repositories.PeopleRepository;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;
    private final AccountsRepository accountsRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository, AccountsRepository accountsRepository) {
        this.peopleRepository = peopleRepository;
        this.accountsRepository = accountsRepository;
    }

    public Optional<Person> findByUsername(String username){
        return peopleRepository.findByUsername(username);
    }

    public Person findById(long id){
        return peopleRepository.findById(id).orElseThrow(PersonNotFoundException::new);
    }
}